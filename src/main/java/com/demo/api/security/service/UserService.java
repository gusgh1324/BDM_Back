package com.demo.api.security.service;

import com.demo.api.dto.UserDTO;
import com.demo.api.entity.User;
import com.demo.api.security.util.JWTUtil;

import java.util.List;
import java.util.Map;

public interface UserService {
  default User dtoToEntity(UserDTO userDTO) {
    User user = User.builder()
            .password(userDTO.getPassword())
            .email(userDTO.getEmail())
            .userImage(userDTO.getUserImage())
            .dataStorage(userDTO.getDataStorage())
            .role(userDTO.getRole())
            .build();
    return user;
  }

  default UserDTO entityToDTO(User user) {
    UserDTO userDTO = UserDTO.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .userImage(user.getUserImage())
            .dataStorage(user.getDataStorage())
            .role(user.getRole())
            .build();
    return userDTO;
  }

  Long registerUser(UserDTO userDTO);

  void updateUser(UserDTO userDTO);

  void removeUser(Long mno);

  UserDTO getUser(Long num);

  List<UserDTO> getAllUsers();

  String login(String email, String pass, JWTUtil jwtUtil);

  Map<String, Object> getGoogleUserInfo(String accessToken);

  User findByEmail(String email);

  void save(User user);
}
