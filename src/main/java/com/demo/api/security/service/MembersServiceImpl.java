package com.demo.api.security.service;

import com.demo.api.dto.MembersDTO;
import com.demo.api.entity.Members;
import com.demo.api.repository.MembersRepository;
import com.demo.api.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Long registMembersDTO(MembersDTO membersDTO) {
    membersDTO.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    return membersRepository.save(dtoToEntity(membersDTO)).getMno();
  }

  @Override
  public void updateMembersDTO(MembersDTO membersDTO) {
    membersRepository.save(dtoToEntity(membersDTO));
  }

  @Override
  public void removeMembers(Long mno) {
    membersRepository.deleteById(mno);
  }

  @Override
  public MembersDTO get(Long num) {
    Optional<Members> result = membersRepository.findById(num);
    if (result.isPresent()) {
      return entityToDTO(result.get());
    }
    return null;
  }

  @Override
  public List<MembersDTO> getAll() {
//    List<Members> membersList = membersRepository.findAll();
    List<Members> membersList = membersRepository.getAll();
    return membersList.stream().map(
        new Function<Members, MembersDTO>() {
          @Override
          public MembersDTO apply(Members members) {
            return entityToDTO(members);
          }
        }
    ).collect(Collectors.toList());
  }

  @Override
  public String login(String email, String pass, JWTUtil jwtUtil) {
    log.info("login................");
    String token = "";
    MembersDTO membersDTO;
    Optional<Members> result = membersRepository.findByEmail(email, false);
    if (result.isPresent()) {
      membersDTO = entityToDTO(result.get());
      log.info("serviceimpl result : " + membersDTO);
      log.info("matches: " + passwordEncoder.matches(pass, membersDTO.getPassword()));
      if (passwordEncoder.matches(pass, membersDTO.getPassword())) {
        try {
          token = jwtUtil.generateToken(email);
          log.info("token : " + token);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return token;
  }

}
