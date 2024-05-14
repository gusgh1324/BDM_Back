package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.repository.PhotoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

  // PhotoRepository 주입
  private final PhotoRepository photoRepository;

  public PhotoServiceImpl(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  @Override
  public void uploadPhoto(PhotoDTO photoDTO) {
    // PhotoDTO를 Photo 엔티티로 변환 후 저장
    // photoRepository.save(photo);
  }

  @Override
  public PhotoDTO getPhotoById(Long id) {
    // id로 Photo 엔티티 조회 후 PhotoDTO로 변환하여 반환
    // photoRepository.findById(id);
    return null;
  }

  @Override
  public List<PhotoDTO> getAllPhotos() {
    // 모든 Photo 엔티티 조회 후 PhotoDTO 리스트로 변환하여 반환
    // photoRepository.findAll();
    return null;
  }

  @Override
  public void deletePhoto(Long id) {
    // id로 Photo 엔티티 조회 후 삭제
    // photoRepository.deleteById(id);
  }
}