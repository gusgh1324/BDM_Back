package com.demo.api.security.service;

import com.demo.api.entity.User;
import com.demo.api.repository.UserRepository;
import com.demo.api.security.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("MembersUserDetailsService loadUserByUsername : " + username);
    Optional<User> result = userRepository.findByEmail(username, false);

    // 사용자가 데이터베이스에 없으면 Exception 발생
    if (!result.isPresent()) throw new UsernameNotFoundException("Check Email or Social ");

    User user = result.get();
    AuthUserDTO authUserDTO = new AuthUserDTO(
        user.getEmail(),
        user.getMno(),
        user.getPassword(),
        false, // 일반 로그인인 경우 false
        user.getRoleSet().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList()),
        null // OAuth2 인증이 아닌 경우 null 전달 ****임시방편
    );
    log.info(">>>" + authUserDTO);
    log.info("authMemberDTO.getAuthorities" + authUserDTO.getAuthorities());
    return authUserDTO;
  }
}
