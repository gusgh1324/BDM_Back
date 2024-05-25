package com.demo.api.service;

import com.demo.api.config.jwt.TokenProvider;
import com.demo.api.dto.PhotoDTO;
import com.demo.api.entity.Photo;
import com.demo.api.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final Path fileStorageLocation;
    private final TokenProvider tokenProvider;

    public PhotoServiceImpl(PhotoRepository photoRepository, @Value("${file.upload-dir}") String uploadDir, TokenProvider tokenProvider) {
        this.photoRepository = photoRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.tokenProvider = tokenProvider;
    }

  @Override
  public PhotoDTO savePhoto(String token, PhotoDTO request) throws Exception {
        MultipartFile file = request.getFile();
        String analysisResult = request.getAnalysisResult();

      // 1. 파일 이름 생성
      String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
      // 2. 파일 저장 경로 설정(file.upload-dir)
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      // 3. 파일 저장
      Files.copy(file.getInputStream(), targetLocation);
      // 4. 파일 URL 생성
      String fileUrl = "http://localhost:8089/server/api/saveAnalysis/" + fileName;

        Long userId = tokenProvider.getMemberId(token);

        Photo photo = Photo.builder()
                .userId(userId)
                .fileUrl(fileUrl)
                .analysisResult(analysisResult)
                .build();

        Photo savedPhoto = photoRepository.save(photo);

    PhotoDTO response = new PhotoDTO();
        response.setUserId(savedPhoto.getUserId());
        response.setFileUrl(savedPhoto.getFileUrl());
        response.setAnalysisResult(savedPhoto.getAnalysisResult());

        return response;
    }
}