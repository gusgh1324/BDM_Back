package com.demo.api.service;

import com.demo.api.dto.PhotoResultDTO;
import java.util.List;

public interface PhotoResultService {
  // 분석 결과 저장
  void savePhotoResult(PhotoResultDTO photoResultDTO);

  // 분석 결과 조회
  PhotoResultDTO getPhotoResultById(Long id);

  // 사진 ID로 분석 결과 리스트 조회
  List<PhotoResultDTO> getPhotoResultsByPhotoId(Long photoId);
}
