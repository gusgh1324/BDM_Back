package com.demo.api.controller;

import com.demo.api.dto.PhotoRequest;
import com.demo.api.entity.Photo;
import com.demo.api.service.PhotoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PhotoController {

  private final PhotoService photoService;

  public PhotoController(PhotoService photoService) {
    this.photoService = photoService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadPhoto(@RequestBody PhotoRequest request) {
    String imageUrl = request.getImageUrl();
    String title = request.getTitle();

    Photo savedPhoto = photoService.savePhoto(imageUrl, title);
    return new ResponseEntity<>(savedPhoto, HttpStatus.CREATED);
  }
}