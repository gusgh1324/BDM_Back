package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
import java.util.List;
public interface PhotoService {
  // 사진 업로드
  void uploadPhoto(PhotoDTO photoDTO);

  // 사진 조회
  PhotoDTO getPhotoById(Long id);

  // 사진 리스트 조회
  List<PhotoDTO> getAllPhotos();

  // 사진 삭제
  void deletePhoto(Long id);
}