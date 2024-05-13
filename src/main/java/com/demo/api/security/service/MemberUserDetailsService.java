package com.demo.api.security.service;

import com.demo.api.entity.Members;
import com.demo.api.repository.MembersRepository;
import com.demo.api.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
  private final MembersRepository membersRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("MembersUserDetailsService loadUserByUsername : " + username);
    Optional<Members> result = membersRepository.findByEmail(username, false);

    // 사용자가 데이터베이스에 없으면 Exception 발생
    if (!result.isPresent()) throw new UsernameNotFoundException("Check Email or Social ");

    Members members = result.get();
    AuthMemberDTO authMemberDTO = new AuthMemberDTO(
        members.getEmail(),
        members.getMno(),
        members.getPassword(),
        members.isFromSocial(),
        members.getRoleSet().stream().map(
            role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList())
    );
    authMemberDTO.setName(members.getName());
    authMemberDTO.setFromSocial(members.isFromSocial());
    log.info(">>>" + authMemberDTO);
    log.info("authMemberDTO.getAuthorities" + authMemberDTO.getAuthorities());
    return authMemberDTO;
  }
}
