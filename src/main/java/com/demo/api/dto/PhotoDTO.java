package com.demo.api.dto;

import com.demo.api.entity.Photo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PhotoDTO {
  private MultipartFile file;
  private String analysisResult;
  private Long userId;
  private String fileUrl;
}