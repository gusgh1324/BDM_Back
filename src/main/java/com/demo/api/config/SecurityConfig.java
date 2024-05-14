package com.demo.api.config;

import com.demo.api.security.filter.ApiCheckFilter;
import com.demo.api.security.filter.ApiLoginFilter;
import com.demo.api.security.handler.ApiLoginFailHandler;
import com.demo.api.security.util.JWTUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private static final String[] PERMIT_ALL_LIST = {
      "/auth/join", "/auth/login",
      "/api/fish-disease/detect",
      "/api/fish-disease/info/**"
      //회원 가입과 로그인은 인증 없이 접근 가능하도록 설정했습니다.
  };

  private static final String[] AUTHENTICATED_LIST = {
      //사용자만 접근 가능하도록 설정했습니다.
      "/api/fish-images/**",
      "/api/detection-history/**",
      "/api/user-profile/**"
  };

  private static final String[] ADMIN_LIST = {
      // ADMIN 역할을 가진 사용자만 접근 가능하도록 설정했습니다.
      "/api/admin/**",
      "/api/fish-disease/manage/**",
      "/api/user-management/**"
  };

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> { // /error : spring에서 기본 제공
      web.ignoring().requestMatchers("/favicon.ico", "/error");
    };
  }

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity, AuthenticationManager am) throws Exception {
//    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    // 주소별 권한 설정
    httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
      authorizationManagerRequestMatcherRegistry.requestMatchers(PERMIT_ALL_LIST).permitAll();
      authorizationManagerRequestMatcherRegistry.requestMatchers(AUTHENTICATED_LIST).authenticated();
      authorizationManagerRequestMatcherRegistry.requestMatchers(ADMIN_LIST).hasAuthority("ADMIN");
      authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
    });

    // UsernamePasswordAuthenticationFilter 이전에 동작하도록 filter chain에 지정
    httpSecurity.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.addFilterBefore(apiLoginFilter(am), UsernamePasswordAuthenticationFilter.class);
    // apiLoginFilter는 ssr방식에서 apiCheckFilter로 가기 전에 사용할 토큰을 발행하는 필터
//    httpSecurity.addFilterBefore(apiLoginFilter(am), UsernamePasswordAuthenticationFilter.class);

    // csrf disable
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    // cors setting
//    httpSecurity.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfig.configurationSource()));

    // cache 없게하기:: 성능 저하가 발생할 수 있다.
    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName(null);
    httpSecurity.requestCache(cache -> cache.requestCache(requestCache));

    // session stateless
    httpSecurity.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JWTUtil jwtUtil() {
    return new JWTUtil();
  }

  // 요청된 클라이언트의 주소에 권한이 있는지 없는지 확인
  @Bean
  public ApiCheckFilter apiCheckFilter() {
    String[] pattern = {"/api/fish-images/*", "/api/detection-history/*",
        "/api/user-profile/*"}; // "/api/user-profile/*"을 추가하여 사용자 프로필 관련 URL도 권한 체크를 하도록 설정
    return new ApiCheckFilter(pattern, jwtUtil());
  }

  @Bean
  public ApiLoginFilter apiLoginFilter(AuthenticationManager am) throws Exception {
    //로그인 요청을 처리
    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/auth/login", jwtUtil());
    apiLoginFilter.setAuthenticationManager(am);
    apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
    return apiLoginFilter;
  }

  @Bean
  // authentication bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
    return ac.getAuthenticationManager();
  }
}
