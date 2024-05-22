package com.demo.api.controller;

import com.demo.api.dto.FishDiseaseRequestDTO;
import com.demo.api.dto.FishDiseaseResponseDTO;
import com.demo.api.entity.FishDiseasePrediction;
import com.demo.api.service.FishDiseasePredictionService;
import com.demo.api.service.FishDiseaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequestMapping("/api/fish-disease")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class FishDiseaseController {
  private final FishDiseaseService fishDiseaseService;
  private final FishDiseasePredictionService predictionService;

  @PostMapping(value = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FishDiseaseResponseDTO> detectFishDisease(
      @RequestParam("image") MultipartFile image,
      @RequestParam("token") String token) {

    // 토큰 검증
    if (!fishDiseaseService.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 이미지 파일 유효성 검사
    if (image.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      // 이미지 파일 저장
      String imagePath = fishDiseaseService.saveImage(image);

      // 파이썬 API에 분석 요청
      FishDiseaseRequestDTO requestDTO = new FishDiseaseRequestDTO();
      requestDTO.setImagePath(imagePath);
      FishDiseaseResponseDTO responseDTO = fishDiseaseService.analyzeFishDisease(requestDTO);

      // 분석 결과를 FishDiseasePrediction으로 변환하여 DB에 저장
      FishDiseasePrediction prediction = FishDiseasePrediction.builder()
          .modelName(responseDTO.getModelName())
          .림포시스티스병(responseDTO.getLymphocystis())
          .비브리오(responseDTO.getVibriosis())
          .아가미흡충(responseDTO.getGillFluke())
          .연쇄구균병(responseDTO.getStreptococcosis())
          .build();
      predictionService.savePrediction(prediction);

      return ResponseEntity.ok(responseDTO);
    } catch (Exception e) {
      log.error("Error occurred while detecting fish disease", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}