package com.demo.api.service;

import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.repository.FishDiseasePredictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FishDiseasePredictionServiceImpl implements FishDiseasePredictionService{

  private final FishDiseasePredictionRepository repository;
  //repository는 FishDiseasePredictionRepository의 인스턴스
  public FishDiseasePredictionServiceImpl(FishDiseasePredictionRepository repository) {
    this.repository = repository;
  }
  @Override
  public List<FishDiseasePrediction> savePredictions(List<FishDiseasePrediction> predictions) {
    return repository.saveAll(predictions); //메서드를 호출하여 예측 결과를 DB에 저장(다수)
  }

  @Override
  public FishDiseasePrediction savePrediction(FishDiseasePrediction prediction) {
    return repository.save(prediction); //메서드를 호출하여 예측 결과를 DB에 저장(단일)
    // 마찬가지로 불필요시 삭제
  }

  @Override
  public List<FishDiseasePrediction> findAllPredictions() {
    return repository.findAll();
  }

  @Override
  public List<FishDiseasePrediction> findByImageUrl(String imageUrl) {
    return repository.findByImageUrl(imageUrl);
  }
}
