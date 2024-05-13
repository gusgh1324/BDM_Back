package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Members extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String id;

  @Column(nullable = false)
  private String password;

  private String state; // 회원의 상태
  private String name;
  private String mobile;
  private String gender;
  private String zipcode;
  private String address;
  private String imgName;
  private String imgUuid;
  private String imgPath;
  private LocalDate birthday; // 생년월일
  private LocalDate stateday; // 상태변경일
  private boolean fromSocial;

  //
  @ElementCollection(fetch= FetchType.LAZY)
  @Builder.Default
  private Set<MembersRole> roleSet = new HashSet<>();

  public void addMembersRole(MembersRole membersRole){
    roleSet.add(membersRole);
  }
}
