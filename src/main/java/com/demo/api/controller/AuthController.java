package com.demo.api.controller;

import com.demo.api.dto.UserDTO;
import com.demo.api.dto.ResponseDTO;
import com.demo.api.security.service.UserService;
import com.demo.api.security.util.JWTUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Validated
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // CORS 설정 추가
public class AuthController {
  private final UserService userService;
  private final JWTUtil jwtUtil;

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  @PostMapping(value = "/join")
  public ResponseEntity<Long> register(@RequestBody UserDTO userDTO) {
    log.info("register..." + userDTO);
    long num = userService.registerUser(userDTO);
    return new ResponseEntity<>(num, HttpStatus.OK);
  }

  @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> getToken(HttpServletResponse response, @RequestBody Map<String, Object> mapObj) {
    String email = mapObj.get("email") != null ? mapObj.get("email").toString() : "";
    String pass = mapObj.get("pass") != null ? mapObj.get("pass").toString() : "";

    log.info(email + "/" + pass);

    if (email.isEmpty() || pass.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    String token = userService.login(email, pass, jwtUtil, false);
    Map<String, String> map = new HashMap<>();

    if (token != null && !token.isEmpty()) {
      map.put("token", token);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    return new ResponseEntity<>(map, HttpStatus.OK);
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
      return new ResponseEntity<>(new ResponseDTO(e.getMessage(), false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/google-login")
  public ResponseEntity<Map<String, String>> googleLogin(@RequestBody Map<String, String> body) {
    String code = body.get("code");
    String redirectUri = body.get("redirectUri");

    if (code == null || code.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      String clientId = getClientId();
      String clientSecret = getClientSecret();

      GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
              GoogleNetHttpTransport.newTrustedTransport(),
              JSON_FACTORY,
              "https://oauth2.googleapis.com/token",
              clientId,
              clientSecret,
              code,
              redirectUri
      ).execute();

      String idTokenString = tokenResponse.getIdToken();
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
              .setAudience(Collections.singletonList(clientId))
              .build();

      GoogleIdToken idToken = verifier.verify(idTokenString);
      if (idToken != null) {
        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");

        if (emailVerified) {
          UserDTO userDTO = userService.findByEmail(email, true);
          if (userDTO == null) {
            userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(""); // 구글 로그인 사용자의 경우 비밀번호는 빈 문자열로 설정
            userDTO.setFromSocial(true);
            userDTO.setName(name); // 이름 설정 추가
            userService.registerUser(userDTO);
          } else if (!userDTO.isFromSocial()) {
            // 동일 이메일이 소셜 로그인이 아닌 경우
            return new ResponseEntity<>(HttpStatus.CONFLICT);
          }

          // 이미 존재하는 소셜 사용자이거나 새로 등록된 사용자에 대해 JWT 생성 및 반환
          String jwt = userService.login(email, "", jwtUtil, true);
          Map<String, String> response = new HashMap<>();
          response.put("token", jwt);
          return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      } else {
        log.warn("Invalid ID token.");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
    } catch (GeneralSecurityException | IOException e) {
      log.error("Google login error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (IllegalArgumentException e) {
      log.error("Invalid ID token format", e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }



  private String getClientId() throws IOException {
    Map<String, String> googleClient = getGoogleClient();
    return googleClient.get("client_id");
  }

  private String getClientSecret() throws IOException {
    Map<String, String> googleClient = getGoogleClient();
    return googleClient.get("client_secret");
  }

  private Map<String, String> getGoogleClient() throws IOException {
    try (FileReader reader = new FileReader("src/main/resources/google-oauth-client.json")) {
      Map<String, Object> jsonMap = new Gson().fromJson(reader, Map.class);
      return (Map<String, String>) jsonMap.get("web");
    }
  }
}

