package com.demo.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.log4j.Log4j2;

// id : 업로드한 이미지를 구분 지을 수 있는 값이면 됨. id는 임시로 지정한 값
// 림포시스티스병, 비브리오, 아가미흡충, 연쇄구균병 : 해당 컬럼의 값은 특정 질병일 확률을 나타내며 단위는 %.

@Entity
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishDiseasePrediction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 프론트에서 업로드한 이미지의 ID

  private String modelName; // 현재 기준 CNN, 전이학습, DNN이 들어갈 예정
  private double 림포시스티스병; // 단위 %
  private double 비브리오; // 단위 %
  private double 아가미흡충; // 단위 %
  private double 연쇄구균병; // 단위 %
}
