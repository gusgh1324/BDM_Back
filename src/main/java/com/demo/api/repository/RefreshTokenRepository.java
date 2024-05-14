package com.demo.api.repository;

import com.demo.api.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUserId(Long userId); // 사용자 ID를 기준으로 리프레시 토큰을 조회하는 기능을 제공
  Optional<RefreshToken> findByRefreshToken(String refreshToken); //리프레시 토큰을 기준으로 리프레시 토큰을 조회하는 기능을 제공

  @Modifying //Modifying, Transactional :  삭제 작업이 트랜잭션 내에서 수행되도록 설정
  @Transactional
  void deleteByUserId(Long mno); //사용자 ID를 기준으로 리프레시 토큰을 삭제하는 기능을 제공
}
