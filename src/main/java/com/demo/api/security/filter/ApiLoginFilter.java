package com.demo.api.security.filter;

import com.demo.api.security.dto.AuthUserDTO;
import com.demo.api.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
  private JWTUtil jwtUtil;
  private PasswordEncoder passwordEncoder;

  public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessesUrl);
    this.jwtUtil = jwtUtil;
    passwordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("ApiLoginFilter..........attemptAuthentication");
    String email = request.getParameter("email");
    String pass = request.getParameter("pass");
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pass);
    log.info("authToken : " + authToken);
    return getAuthenticationManager().authenticate(authToken);
//    if (email == null) {
//      throw new BadCredentialsException("email cannot be null");
//    }
//    return null;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
//    log.info("ApiLoginFilter..........successfulAuthentication...authResult:" + authResult);
    log.info("authResult.getPrincipal(): " + authResult.getPrincipal());
    String email = ((AuthUserDTO) (authResult.getPrincipal())).getUsername();
    String token = null;
    try {
      token = jwtUtil.generateToken(email);
      response.setContentType("text.plain");
      response.getOutputStream().write(token.getBytes());
      log.info("token : " + token);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
