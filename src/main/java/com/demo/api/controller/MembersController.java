package com.demo.api.controller;

import com.demo.api.dto.MembersDTO;
import com.demo.api.security.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/members/")
@RequiredArgsConstructor
public class MembersController {
  private final MembersService membersService;

  @PutMapping(value = "/update", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> update(@RequestBody MembersDTO membersDTO) {
    log.info("update..." + membersDTO);
    membersService.updateMembersDTO(membersDTO);
    return new ResponseEntity<>("modified", HttpStatus.OK);
  }

  @DeleteMapping(value = "/delete/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> delete(@PathVariable("num") Long mno) {
    log.info("delete......");
    membersService.removeMembers(mno);
    return new ResponseEntity<>("deleted", HttpStatus.OK);
  }

  @GetMapping(value = "/get/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MembersDTO> read(@PathVariable("num") Long num) {
    log.info("read......" + num);
    return new ResponseEntity<>(membersService.get(num), HttpStatus.OK);
  }

  @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MembersDTO>> getAll() {
    log.info("getList......");
    return new ResponseEntity<>(membersService.getAll(), HttpStatus.OK);
  }
/*
  {
    "id":"user105@example.com",
      "password":"1",
      "name":"사용자105",
      "gender":"남",
      "email":"user105@example.com",
      "mobile":"010-1234-1234",
      "fromSocial":false,
      "roleSet":["ROLE_USER"]
  }
*/
}
