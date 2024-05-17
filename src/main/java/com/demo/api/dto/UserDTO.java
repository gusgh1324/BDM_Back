package com.demo.api.dto;

import com.demo.api.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
  private String email;
  private String password;
  private String userImage;
  private Long dataStorage;
  private String name;
  private UserRole role; //role 필드 추가
  private boolean fromSocial = false; // 기본값을 false로 설정
}
//UserDTO에는 id와 role 필드가 없다.
// 이는 클라이언트에게 전달할 때 민감한 정보를 제외하기 위함.