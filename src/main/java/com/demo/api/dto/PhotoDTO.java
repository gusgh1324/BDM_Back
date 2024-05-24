package com.demo.api.dto;

import com.demo.api.entity.Photo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoDTO {
  private String title;
  private String imageUrl;

  public Photo toEntity() {
    return Photo.builder()
        .title(extractAnalysisResult(title))
        .imageUrl(imageUrl)
        .build();
  }
  private String extractAnalysisResult(String url) {
    int index = url.indexOf("?");
    if (index != -1) {
      return url.substring(index + 1);
    }
    return url;
  }
}