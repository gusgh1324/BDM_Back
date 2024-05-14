package com.demo.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass //상속받는 클래스들이 이 클래스의 필드를 가지도록 합니다.
@EntityListeners(AuditingEntityListener.class) //엔티티의 생성일자와 수정일자를 자동으로 관리
public class BaseEntity {
  @CreatedDate //값을 자동으로 설정
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt; //필드를 정의

  @LastModifiedDate //값을 자동으로 설정
  @Column(name = "updated_at")
  private LocalDateTime updatedAt; //필드를 정의
}