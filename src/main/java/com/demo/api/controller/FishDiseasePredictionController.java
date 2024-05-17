package com.demo.api.controller;

import com.demo.api.dto.FishDiseasePredictionDTO;
import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.service.FishDiseasePredictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

  // 분석 결과를 DB에 저장
  @PostMapping
  public List<FishDiseasePrediction> addPredictions(@RequestBody List<FishDiseasePrediction> predictions) {
    return service.savePredictions(predictions);
  }
}