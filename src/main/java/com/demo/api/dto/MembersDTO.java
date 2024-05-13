package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersDTO {
  private Long mno;
  private String id;
  private String password;
  private String name;
  private String gender;
  private String email;
  private String mobile;
  private String imgName;
  private String imgUuid;
  private String imgPath;
  private LocalDate birthday; // 생년월일
  private LocalDate stateday; // 상태변경일
  private boolean fromSocial;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
  @Builder.Default
  private Set<String> roleSet = new HashSet<>();
}
