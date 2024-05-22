package com.demo.api.service;

import com.demo.api.dto.FishDiseasePredictionDTO;
import com.demo.api.entity.FishDiseasePrediction;

import java.util.List;

// 파이썬 파일로 분석한 이미지 데이터 결과값을 DB로 저장하는 작업을 처리하는 서비스


public interface FishDiseasePredictionService {
  default FishDiseasePrediction dtoToEntity(FishDiseasePredictionDTO fishDiseasePredictionDTO) {
    FishDiseasePrediction fishDiseasePrediction = FishDiseasePrediction.builder()
        .id(fishDiseasePredictionDTO.getId())
        .modelName(fishDiseasePredictionDTO.getModelName())
        .림포시스티스병(fishDiseasePredictionDTO.get림포시스티스병())
        .비브리오(fishDiseasePredictionDTO.get비브리오())
        .아가미흡충(fishDiseasePredictionDTO.get아가미흡충())
        .연쇄구균병(fishDiseasePredictionDTO.get연쇄구균병())
        .build();
    return fishDiseasePrediction;
  }

  default FishDiseasePredictionDTO entityToDTO(FishDiseasePrediction fishDiseasePrediction) {
    FishDiseasePredictionDTO fishDiseasePredictionDTO = FishDiseasePredictionDTO.builder()
        .id(fishDiseasePrediction.getId())
        .modelName(fishDiseasePrediction.getModelName())
        .림포시스티스병(fishDiseasePrediction.get림포시스티스병())
        .비브리오(fishDiseasePrediction.get비브리오())
        .아가미흡충(fishDiseasePrediction.get아가미흡충())
        .연쇄구균병(fishDiseasePrediction.get연쇄구균병())
        .build();
    return fishDiseasePredictionDTO;
  }

  // 다중 분석 결과를 DB에 저장
  List<FishDiseasePrediction> savePredictions(List<FishDiseasePrediction> predictions);

  // 단일 분석 결과를 DB에 저장(불필요시 삭제해도됨)
  FishDiseasePrediction savePrediction(FishDiseasePrediction prediction);

  // selectAll
  List<FishDiseasePrediction> findAllPredictions();

  // select
  List<FishDiseasePrediction> findByImageUrl(String imageUrl);
}
