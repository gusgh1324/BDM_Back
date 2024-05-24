package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
public interface PhotoService {
  PhotoDTO savePhoto(String token, PhotoDTO request) throws Exception;
}