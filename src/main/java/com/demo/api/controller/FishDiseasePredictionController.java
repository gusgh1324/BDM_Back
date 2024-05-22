package com.demo.api.controller;

import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.service.FishDiseasePredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

// 파이썬 파일로 분석한 이미지 데이터 결과값을 DB로 저장하는 작업을 처리하는 컨트롤러

@Log4j2
@Validated
@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class FishDiseasePredictionController {

  private final FishDiseasePredictionService service;

  @Value("${python.script.path}")
  private String pythonScriptPath;

  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  private SseEmitter emitter = new SseEmitter();

  // 리액트에서 업로드한 이미지를 전달받아 파이썬 파일로 이미지 데이터를 전달시켜 이미지 분류 시작
  @PostMapping(value = "/analyze_image", consumes = "multipart/form-data")
  public ResponseEntity<?> analyzeImage(@RequestParam("image") MultipartFile image) {
    try {
      Path tempFile = Files.createTempFile("uploaded_image", image.getOriginalFilename());
      image.transferTo(tempFile);
      reloadEmitter(); // 생성자에서 이미터 초기화

      ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, tempFile.toString());
      Process process = pb.start();

      executor.submit(() -> {
        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
             BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {

          String line;
          while ((line = stdOutReader.readLine()) != null) {
            System.out.println("STDOUT: " + line);  // 콘솔에 출력
            emitter.send(SseEmitter.event().name("progress").data(line));
          }

          while ((line = stdErrReader.readLine()) != null) {
            System.err.println("STDERR: " + line);  // 콘솔에 출력
            emitter.send(SseEmitter.event().name("progress").data(line));
          }
        } catch (IOException e) {
          emitter.completeWithError(e);
        }
      });

      int exitCode = process.waitFor();

      Files.delete(tempFile);

      if (exitCode == 0) {
        emitter.send(SseEmitter.event().name("complete").data("Analysis complete"));
        return ResponseEntity.ok("Analysis complete");
      } else {
        emitter.send(SseEmitter.event().name("error").data("Image analysis failed"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image analysis failed with exit code " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      log.error("Error during image analysis", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during image analysis: " + e.getMessage());
    }
  }

  //별도의 이미터를 보내는 get요청을 작성
  @GetMapping("/stream")
  public SseEmitter stream() {
    if (emitter == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return emitter;
  }

  // 사진 분석 결과를 DB에 저장
  @PostMapping("/save")
  public List<FishDiseasePrediction> addPredictions(@RequestBody List<FishDiseasePrediction> predictions) {
    return service.savePredictions(predictions);
  }

  // DB에 저장된 사진 분석결과를 전부 불러오기
  @GetMapping("/latest")
  public List<FishDiseasePrediction> getLatestPredictions() {
    return service.findAllPredictions();
  }

  @PostMapping(value = "/select", consumes = "multipart/form-data")
  public List<FishDiseasePrediction> sendPredictionToClient(@RequestParam("image") MultipartFile image) throws IOException {
    Path tempFile = Files.createTempFile("uploaded_image", image.getOriginalFilename());
    String imageUrl = tempFile.toString();
    return service.findByImageUrl(imageUrl);
  }

  // 이미터를 다시 생성하는 메서드
  private void reloadEmitter() {
    emitter = new SseEmitter();
  }

}