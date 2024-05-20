package com.demo.api.service;

import com.demo.api.dto.FishDiseaseRequestDTO;
import com.demo.api.dto.FishDiseaseResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FishDiseaseServiceImpl implements FishDiseaseService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${python.api.url}") //파이썬 API의 URL을 설정
  private String pythonApiUrl;

  private final RestTemplate restTemplate;

  public FishDiseaseServiceImpl(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  public boolean validateToken(String token) {
    // 토큰 검증 로직 구현
    return true; // 임시로 항상 true 반환
  }

  @Override
  public String saveImage(MultipartFile image) throws IOException {
    Path uploadPath = Paths.get(uploadDir);

    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String originalFileName = image.getOriginalFilename();
    String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

    Path filePath = uploadPath.resolve(fileName);
    image.transferTo(filePath);

    return filePath.toString();
  }

  @Override
  public FishDiseaseResponseDTO analyzeFishDisease(FishDiseaseRequestDTO requestDTO) {
    // 파이썬 API 호출
    ResponseEntity<Map> response = restTemplate.postForEntity(pythonApiUrl, requestDTO, Map.class);
    // 파이썬 API를 호출하고 응답 결과를 처리

    // 응답 결과 처리 정상 : 200, 응답 본문에서 필요한 값을 추출하여 FishDiseaseResponseDTO 객체에 설정하고 반환
                // 비정상 : RuntimeException을 발생시키고 상태 코드를 포함한 오류 메시지를 출력
    if (response.getStatusCode() == HttpStatus.OK) {
      Map<String, Object> resultMap = response.getBody();

      FishDiseaseResponseDTO responseDTO = new FishDiseaseResponseDTO();
      responseDTO.setModelName((String) resultMap.get("model_name"));
      responseDTO.setLymphocystis((Double) resultMap.get("lymphocystis"));
      responseDTO.setVibriosis((Double) resultMap.get("vibriosis"));
      responseDTO.setGillFluke((Double) resultMap.get("gill_fluke"));
      responseDTO.setStreptococcosis((Double) resultMap.get("streptococcosis"));

      return responseDTO;
    } else {
      throw new RuntimeException("Failed to analyze fish disease. Status code: " + response.getStatusCode());
    }
  }
}

//결과적으로 이 코드는 파일 업로드 기능과 파이썬 API 호출 기능을 모두 가지고 있음.


// 주의1. 파이썬 API의 요청/응답 형식에 맞게 FishDiseaseRequestDTO와 FishDiseaseResponseDTO 클래스를 적절히 수정


// 주의2. application.properties 또는 application.yml 파일에 file.upload-dir과 python.api.url 속성을 설정
//ex)
//file.upload-dir=/path/to/upload/directory
//python.api.url=http://localhost:5000/analyze