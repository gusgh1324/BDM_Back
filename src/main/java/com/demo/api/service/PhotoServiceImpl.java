package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.entity.Photo;
import com.demo.api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  // PhotoRepository 주입
  private final PhotoRepository photoRepository;

  @Override
  @Transactional
  public void savePhoto(PhotoDTO photoDTO) {
    Photo photo = photoDTO.toEntity();
    photoRepository.save(photo);
  }
}