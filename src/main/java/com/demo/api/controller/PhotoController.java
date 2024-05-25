package com.demo.api.controller;

import com.demo.api.dto.PhotoDTO;
import com.demo.api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saveAnalysis")
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class PhotoController {

  @Value("${file.upload-dir}")
  private String uploadDirectory;

  private final PhotoService photoService;

  @PostMapping("/save")
  public PhotoDTO savePhoto(@RequestHeader("Authorization") String token,
                            @ModelAttribute PhotoDTO request) throws Exception {
    return photoService.savePhoto(token.replace("Bearer ", ""), request);
  }

  @GetMapping("/{filename}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) {
    try {
      Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists()) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }
}