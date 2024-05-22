package com.demo.api.repository;

import com.demo.api.entity.FishDiseasePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 파이썬 파일로 분석한 이미지 데이터 결과값을 DB로 저장하는 작업을 처리하는 리포지토리

public interface FishDiseasePredictionRepository extends JpaRepository<FishDiseasePrediction, Long> {
  List<FishDiseasePrediction> findByImageUrl(String imageUrl);
}

