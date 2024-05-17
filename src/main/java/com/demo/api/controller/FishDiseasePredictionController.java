package com.demo.api.controller;

import com.demo.api.dto.FishDiseasePredictionDTO;
import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.service.FishDiseasePredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

// 파이썬 파일로 분석한 이미지 데이터 결과값을 DB로 저장하는 작업을 처리하는 컨트롤러

@Log4j2
@Validated
@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class FishDiseasePredictionController {

  private final FishDiseasePredictionService service;

  // 리액트에서 업로드한 이미지를 전달받아 파이썬 파일로 이미지 데이터를 전달시켜 이미지 분류 시작
  // (상황에 따라 별도의 컨트롤러로 옮기거나 코드 내용 수정 가능)
  @PostMapping("/analyze_image")
  public void analyzeImage(@RequestParam("image") MultipartFile image) {
    try {
      // 업로드된 이미지를 임시 파일로 저장
      Path tempFile = Files.createTempFile("uploaded_image", image.getOriginalFilename());
      image.transferTo(tempFile);

      // Python 스크립트 실행
      ProcessBuilder pb = new ProcessBuilder("python", "analyze_image.py", tempFile.toString());
      Process process = pb.start();

      // 임시 파일 삭제
      Files.delete(tempFile);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("이미지 분석 중 오류 발생");
    }
  }

  // 분석 결과를 DB에 저장
  @PostMapping
  public List<FishDiseasePrediction> addPredictions(@RequestBody List<FishDiseasePrediction> predictions) {
    return service.savePredictions(predictions);
  }
}