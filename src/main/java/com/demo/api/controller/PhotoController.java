package com.demo.api.controller;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saveAnalysis")
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class PhotoController {

  private final PhotoService photoService;
  @Value("${python.script.path}")
  private String pythonScriptPath;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private SseEmitter emitter;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void savePhoto(@RequestBody PhotoDTO photoDTO) {
    photoService.savePhoto(photoDTO);
  }

  @PostMapping(value = "/analyze_image_p", consumes = "multipart/form-data")
  public ResponseEntity<?> analyzeImage(@RequestParam("file") MultipartFile image) {
    try {
      Path tempFile = Files.createTempFile("uploaded_image", image.getOriginalFilename());
      image.transferTo(tempFile);

      reloadEmitter(); // 생성자에서 이미터 초기화

      ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, tempFile.toString());
      Process process = pb.start();

      executor.submit(() -> {
        StringBuilder results = new StringBuilder();

        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
             BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
          String line;
          while ((line = stdOutReader.readLine()) != null) {
            System.out.println("STDOUT: " + line);
            emitter.send(SseEmitter.event().name("progress").data(line));
            if (line.startsWith("RESULT:")) {
              results.append(line.substring(7)); // "RESULT:" 이후의 부분을 저장
            }
          }
          while ((line = stdErrReader.readLine()) != null) {
            System.err.println("STDERR: " + line);
          }
        } catch (IOException e) {
          log.error("Error reading process output", e);
          emitter.completeWithError(e);
          return; // 예외 발생 시 작업을 중단
        }

        try {
          int exitCode = process.waitFor();
          Files.delete(tempFile);
          if (exitCode == 0) {
            String analysisResultFromPython = results.toString();
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setTitle(analysisResultFromPython);
            photoDTO.setImageUrl(image.getOriginalFilename());
            photoService.savePhoto(photoDTO);

            emitter.send(SseEmitter.event().name("complete").data("Analysis complete"));
            emitter.send(SseEmitter.event().name("result").data(analysisResultFromPython)); // 최종 결과 전송
            emitter.complete();
          } else {
            emitter.send(SseEmitter.event().name("error").data("Image analysis failed"));
            emitter.complete();
          }
        } catch (InterruptedException e) {
          log.error("Error during process execution", e);
          emitter.completeWithError(e);
        } catch (IOException e) {
          log.error("Error during file deletion", e);
          emitter.completeWithError(e);
        }
      });

      return ResponseEntity.ok("Image analysis started");
    } catch (IOException e) {
      log.error("Error during image analysis", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during image analysis: " + e.getMessage());
    }
  }

  private void reloadEmitter() {
    emitter = new SseEmitter();
  }
}