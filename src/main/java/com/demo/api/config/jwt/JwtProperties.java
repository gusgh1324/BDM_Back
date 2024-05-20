package com.demo.api.config.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtProperties {
  private String issuer;
  private String secretKey;
}
