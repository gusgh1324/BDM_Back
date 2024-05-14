package com.demo.api.security.service;

import com.demo.api.dto.UserDTO;
import com.demo.api.entity.User;
import com.demo.api.repository.UserRepository;
import com.demo.api.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Long registerUser(UserDTO userDTO) {
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    return userRepository.save(dtoToEntity(userDTO)).getMno();
  }

  @Override
  public void updateUser(UserDTO userDTO) {
    userRepository.save(dtoToEntity(userDTO));
  }

  @Override
  public void removeUser(Long mno) {
    userRepository.deleteById(mno);
  }

  @Override
  public UserDTO getUser(Long num) {
    Optional<User> result = userRepository.findById(num);
    return result.map(this::entityToDTO).orElse(null);
  }

  @Override
  public List<UserDTO> getAllUsers() {
    List<User> userList = userRepository.getAll();
    return userList.stream()
        .map(this::entityToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public String login(String email, String pass, JWTUtil jwtUtil) {
    log.info("login................");
    String token = "";
    UserDTO userDTO;
    Optional<User> result = userRepository.findByEmail(email, false);
    if (result.isPresent()) {
      userDTO = entityToDTO(result.get());
      log.info("serviceimpl result : " + userDTO);
      log.info("matches: " + passwordEncoder.matches(pass, userDTO.getPassword()));
      if (passwordEncoder.matches(pass, userDTO.getPassword())) {
        try {
          token = jwtUtil.generateToken(email);
          log.info("token : " + token);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        throw new BadCredentialsException("Invalid password");
      }
    } else {
      throw new UsernameNotFoundException("User not found");
    }
    return token;
  }

}

