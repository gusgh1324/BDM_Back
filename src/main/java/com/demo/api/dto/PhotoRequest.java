package com.demo.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoRequest {
  private Long id;
  private String imageUrl;
  private String title;
  private Long userId;
}