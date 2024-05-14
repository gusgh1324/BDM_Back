package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity { //리프레시 토큰을 저장하는 엔티티 클래스
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name = "token", nullable = false)
  private String token;

  public void update(String token){
    this.token = token;
  }
}
