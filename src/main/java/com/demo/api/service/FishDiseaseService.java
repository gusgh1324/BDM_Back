package com.demo.api.service;

import com.demo.api.dto.FishDiseaseRequestDTO;
import com.demo.api.dto.FishDiseaseResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FishDiseaseService {
  boolean validateToken(String token);

  // 이미지 파일 저장
  String saveImage(MultipartFile image) throws IOException;
  FishDiseaseResponseDTO analyzeFishDisease(FishDiseaseRequestDTO requestDTO);
}