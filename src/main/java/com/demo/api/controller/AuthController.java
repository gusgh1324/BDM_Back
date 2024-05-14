package com.demo.api.controller;

import com.demo.api.dto.UserDTO;
import com.demo.api.dto.ResponseDTO;
import com.demo.api.security.service.UserService;
import com.demo.api.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Validated
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserService userService;
  private final JWTUtil jwtUtil;

  @PostMapping(value = "/join")
  public ResponseEntity<Long> register(@RequestBody UserDTO userDTO) {
    log.info("register..." + userDTO);
    long num = userService.registerUser(userDTO);
    return new ResponseEntity<>(num, HttpStatus.OK);
  }

  @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> getToken(HttpServletResponse response, @RequestBody Map<String, Object> mapObj) {
    String email = mapObj.get("email").toString();
    String pass = mapObj.get("pass").toString();
    log.info(email + "/" + pass);
    String token = userService.login(email, pass, jwtUtil);
    Map<String, String > map= new HashMap<>();
    if (token != "" && token.length() > 1) {
      /*try {
        response.setContentType("text/plain");
        response.getOutputStream().write(token.getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }*/
        map.put("token", token);
    }
    return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
    try {
      log.info(request);
      log.info(response);
      log.info(SecurityContextHolder.getContext().getAuthentication());
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
      return new ResponseEntity<>(new ResponseDTO("success", true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseDTO(e.getMessage(), false), HttpStatus.NOT_FOUND);
    }
  }
}
