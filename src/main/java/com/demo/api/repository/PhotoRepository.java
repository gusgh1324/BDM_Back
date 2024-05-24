package com.demo.api.repository;

import com.demo.api.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    //  Photo 엔티티에 대한 저장, 조회, 삭제 작업이 수행될 때, 실제로 데이터베이스의 photo 테이블에 반영
}
