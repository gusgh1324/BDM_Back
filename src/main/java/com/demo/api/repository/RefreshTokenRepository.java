package com.demo.api.repository;

import com.demo.api.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUserId(Long userId);
  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  @Modifying
  @Transactional
  void deleteByUserId(Long mno);
}
