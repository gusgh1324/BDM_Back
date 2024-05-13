package com.demo.api.repository;

import com.demo.api.entity.Members;
import com.demo.api.entity.MembersRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
@SpringBootTest
class MembersRepositoryTests {
  @Autowired
  private MembersRepository membersRepository;
  @Autowired
  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Test
  public void insertDummies(){
    IntStream.rangeClosed(1,100).forEach(new IntConsumer() {
      @Override
      public void accept(int i) {
        Members members = Members.builder()
            .email("user" + i + "@example.com")
            .id("user" + i + "@example.com")
            .name("사용자" + i)
            .gender(new String[]{"여", "남"}[(int)(Math.random()*2)])
            .birthday(LocalDate.of((int)(Math.random()*20)+1990, (int)(Math.random()*12)+1, (int)(Math.random()*28)+1))
            .fromSocial(false)
            .password(passwordEncoder().encode("1"))
            .state("0") // 0은 가입상태, 1은 , 2는 관리자
            .build();
        members.addMembersRole(MembersRole.USER);
        if(i>=80) members.addMembersRole(MembersRole.MANAGER);
        if(i>=90) members.addMembersRole(MembersRole.ADMIN);
        membersRepository.save(members);
      }
    });
  }
}