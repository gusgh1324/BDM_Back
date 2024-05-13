package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false)
  private String refreshToken;

  public RefreshToken update(String refreshToken){
    this.refreshToken = refreshToken;
    return this;
  }
}
