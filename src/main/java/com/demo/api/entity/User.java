package com.demo.api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;

  @Column(unique = true)
  private String name;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "user_image")
  private String userImage;

  @Column(name = "data_storage")
  private Long dataStorage;

  @Column(name = "from_social")
  private boolean fromSocial;

  //role 필드는 사용자의 역할을 나타내며, UserRole 열거형을 사용합니다.
  // @Enumerated 어노테이션을 사용하여 열거형과 문자열 간의 매핑을 설정
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;


  @ElementCollection(fetch= FetchType.LAZY)
  @Builder.Default
  private Set<UserRole> roleSet = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Photo> photos = new ArrayList<>();

  //패스워드 변경, 사용자 이미지 업데이트, 데이터 스토리지 업데이트를 위한 메서드를 추가로 정의했습니다.
  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateUserImage(String userImage) {
    this.userImage = userImage;
  }

  public void updateDataStorage(Long dataStorage) {
    this.dataStorage = dataStorage;
  }
}