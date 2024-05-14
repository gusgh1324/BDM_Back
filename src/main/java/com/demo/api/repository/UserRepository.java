package com.demo.api.repository;

import com.demo.api.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//  @EntityGraph(attributePaths =)
  @Query("select u from User u ")
  List<User> getAll(); //모든 사용자를 조회하는 기능을 제공

  @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
  // EntityGraph : roleSet 속성을 즉시 로딩(EAGER)하도록 설정
  @Query("select u from User u where u.fromSocial = :social and u.email=:email")
  Optional<User> findByEmail(String email, boolean social);//이메일 기준으로 사용자를 조회하는 기능을 제공

  @EntityGraph(attributePaths = {"roleSet"}, type=EntityGraph.EntityGraphType.LOAD)
  // EntityGraph : roleSet 속성을 즉시 로딩(EAGER)하도록 설정
  @Query("select u from User u where u.fromSocial = :social and u.id= :id")
  Optional<User> findById(String id, boolean social);//ID를 기준으로 사용자를 조회하는 기능을 제공

  @Query("select u from User u where u.id = :id and u.password = :password")
  Optional<User> findByIdAndPassword(@Param("id") String id, @Param("password") String pass);
  // ID와 비밀번호를 기준으로 사용자를 조회하는 기능을 제공
}