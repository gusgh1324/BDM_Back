package com.demo.api.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class AuthUserDTO extends User implements OAuth2User {
  private String email;
  private Long mno;
  private String password;
  private String name;
  private boolean fromSocial;
  private Map<String, Object> attr;

  public AuthUserDTO(String username, Long mno, String name, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities){
    super(username, password, authorities);
    this.email = username; // security 사용자가 생성한 계정을 DB의 계정과 연결하는 부분(중요)
    this.mno = mno;
    this.name = name;
    this.fromSocial = fromSocial;
  }
  public AuthUserDTO(String username, Long mno, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
    this(username, mno, null, password, fromSocial, authorities);
    this.attr = attr;
  }



  @Override
  public Map<String, Object> getAttributes() {
    return this.attr;
  }
}
