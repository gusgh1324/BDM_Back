package com.demo.api.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil { // JSON 웹 토큰 생성을 위한 클래스
  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.issuer}")
  private String issuer;

  private long expire = 60 * 24 * 30;

  public String generateToken(String content) throws Exception {
    return Jwts.builder()
        .claim("sub", content) //토큰이 보관할 컨텐츠
        .issuer(issuer) // 이슈어 설정
        .issuedAt(new Date()) // 생성일
        .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) // 만기일
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // 암호화
        .compact();
  }

  public String validateAndExtract(String tokenStr) throws Exception {
    log.info("Jwt getClass: " +
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build().parse(tokenStr));
    Claims claims = (Claims) Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))).build().parse(tokenStr).getPayload();
    return (String) claims.get("sub");
  }

}
