package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.entity.Photo;
import com.demo.api.repository.PhotoRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

  private final PhotoRepository photoRepository;

  @Autowired
  public PhotoServiceImpl(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  @Override
  public Photo savePhoto(String imageUrl, String title) {
    Photo photo = Photo.builder()
        .imageUrl(imageUrl)
        .title(title)
        .build();
    return photoRepository.save(photo);
  }
}