package com.demo.api.service;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
public interface PhotoService {
  Photo savePhoto(String imageUrl, String title);

}