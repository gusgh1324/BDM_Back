package com.demo.api.config.jwt;

import com.demo.api.entity.User;
import com.demo.api.entity.RefreshToken;
import com.demo.api.repository.RefreshTokenRepository;
import com.demo.api.security.dto.AuthUserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class TokenProvider {
  private final JwtProperties jwtProperties;
  private Key key;
  private final RefreshTokenRepository refreshTokenRepository;
  // jwt로 mno 추출
  public Long getMemberId(String token){
    Claims claims = getClaims(token);
    return claims.get("id", Long.class);
  }

  // jwt로 email 추출
  public String getMemeberEmail(String token){
    Claims claims = getClaims(token);
    return claims.get("sub", String.class);
  }

  // jwt parsing method
  private Claims getClaims(String token) {
    key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());

    return Jwts.parser()
        .verifyWith((SecretKey) key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
