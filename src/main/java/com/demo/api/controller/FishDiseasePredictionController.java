package com.demo.api.controller;

import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.service.FishDiseasePredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

  // 리액트에서 업로드한 이미지를 전달받아 파이썬 파일로 이미지 데이터를 전달시켜 이미지 분류 시작
  // (상황에 따라 별도의 컨트롤러로 옮기거나 코드 내용 수정 가능)
  @PostMapping("/analyze_image")
  public SseEmitter analyzeImage(@RequestParam("image") MultipartFile image) {
    SseEmitter emitter = new SseEmitter();
    ExecutorService executor = Executors.newSingleThreadExecutor();

    executor.submit(() -> {
      try {
        // 업로드된 이미지를 임시 파일로 저장
        Path tempFile = Files.createTempFile("uploaded_image", image.getOriginalFilename());
        image.transferTo(tempFile);

        // Python 스크립트 실행
        ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, tempFile.toString());
        Process process = pb.start();

        // 파이썬 스크립트의 출력을 읽기
        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

          // 표준 출력 스트림 읽기
          String line;
          while ((line = stdOutReader.readLine()) != null) {
            System.out.println("STDOUT: " + line);  // 콘솔에 출력
            emitter.send(SseEmitter.event().name("progress").data(line));
          }

          // 표준 오류 스트림 읽기
          while ((line = stdErrReader.readLine()) != null) {
            System.err.println("STDERR: " + line);  // 콘솔에 출력
            emitter.send(SseEmitter.event().name("error").data(line));
          }
        }

        // 프로세스가 종료될 때까지 대기
        int exitCode = process.waitFor();
        executor.shutdown();

        // 임시 파일 삭제
        Files.delete(tempFile);

        // 종료 코드 확인 및 결과값 반환
        if (exitCode == 0) {
          emitter.send(SseEmitter.event().name("complete").data("Analysis complete"));
        } else {
          emitter.send(SseEmitter.event().name("error").data("Image analysis failed"));
        }
        emitter.complete();
      } catch (IOException | InterruptedException e) {
        try {
          emitter.send(SseEmitter.event().name("error").data("Error occurred during image analysis"));
          emitter.completeWithError(e);
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
    });

    return emitter;
  }

  @PostMapping
  public List<FishDiseasePrediction> addPredictions(@RequestBody List<FishDiseasePrediction> predictions) {
    return service.savePredictions(predictions);
  }
}