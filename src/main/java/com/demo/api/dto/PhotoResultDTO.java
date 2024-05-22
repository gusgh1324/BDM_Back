package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResultDTO {
  private Long id; //분석 결과의 고유 식별자
  private String resultText; //분석 결과 텍스트
  private Long photoId; //분석 대상이 된 사진의 고유 식별자
}