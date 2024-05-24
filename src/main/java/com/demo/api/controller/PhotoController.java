package com.demo.api.controller;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saveAnalysis")
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class PhotoController {

  private final PhotoService photoService;

  @PostMapping("/save")
  public PhotoDTO savePhoto(@RequestHeader("Authorization") String token,
                            @ModelAttribute PhotoDTO request) throws Exception {
    return photoService.savePhoto(token.replace("Bearer ", ""), request);
  }
}