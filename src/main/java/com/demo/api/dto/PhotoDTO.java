package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDTO {
  private Long id; //사진의 고유 식별자
  private String title; //사진의 제목
  private String imageUrl; //사진의 URL 주소
}