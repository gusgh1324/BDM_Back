package com.demo.api.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil { // JSON 웹 토큰 생성을 위한 클래스
  private String secretKey = "1234567890abcdefghijklmnopqrstuvwxyz";
  private long expire = 60 * 24 * 30;

  public String generateToken(String content) throws Exception {
    return Jwts.builder()
        .issuedAt(new Date()) // 생성일
        .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) // 만기일
        .claim("sub", content)
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
