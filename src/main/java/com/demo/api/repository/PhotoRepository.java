package com.demo.api.repository;

import com.demo.api.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // 추가적인 쿼리 메서드 정의 가능
}
