package com.demo.api.repository;

import com.demo.api.entity.PhotoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoResultRepository extends JpaRepository<PhotoResult, Long> {
    List<PhotoResult> findByPhotoId(Long photoId);
}
