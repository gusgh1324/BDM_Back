package com.demo.api.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter //setModelName, setLymphocystis, setVibriosis, setGillFluke, setStreptococcosis 메서드가 자동으로 생성
public class FishDiseaseResponseDTO {
  private String modelName;
  private double lymphocystis;
  private double vibriosis;
  private double gillFluke;
  private double streptococcosis;
  // 필요한 다른 필드 추가
}