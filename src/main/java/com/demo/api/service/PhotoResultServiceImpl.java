package com.demo.api.service;

import com.demo.api.dto.PhotoResultDTO;
import com.demo.api.repository.PhotoResultRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PhotoResultServiceImpl implements PhotoResultService {

  // PhotoResultRepository 주입
  private final PhotoResultRepository photoResultRepository;

  public PhotoResultServiceImpl(PhotoResultRepository photoResultRepository) {
    this.photoResultRepository = photoResultRepository;
  }

  @Override
  public void savePhotoResult(PhotoResultDTO photoResultDTO) {
    // PhotoResultDTO를 PhotoResult 엔티티로 변환 후 저장
    // photoResultRepository.save(photoResult);
  }

  @Override
  public PhotoResultDTO getPhotoResultById(Long id) {
    // id로 PhotoResult 엔티티 조회 후 PhotoResultDTO로 변환하여 반환
    // photoResultRepository.findById(id);
    return null;
  }

  @Override
  public List<PhotoResultDTO> getPhotoResultsByPhotoId(Long photoId) {
    // photoId로 PhotoResult 엔티티 리스트 조회 후 PhotoResultDTO 리스트로 변환하여 반환
    // photoResultRepository.findByPhotoId(photoId);
    return null;
  }
}
