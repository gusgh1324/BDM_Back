package com.demo.api.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/test")
public class TestController {
  @GetMapping("/info")
  public ResponseEntity<String> info() {
    return new ResponseEntity<>("hello", HttpStatus.OK);
  }
}
