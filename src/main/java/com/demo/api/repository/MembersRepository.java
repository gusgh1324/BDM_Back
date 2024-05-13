package com.demo.api.repository;

import com.demo.api.entity.Members;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {
//  @EntityGraph(attributePaths =)
  @Query("select m from Members m ")
  List<Members> getAll();

  @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
  @Query("select m from Members m where m.fromSocial = :social and m.email=:email")
  Optional<Members> findByEmail(String email, boolean social);

  @EntityGraph(attributePaths = {"roleSet"}, type=EntityGraph.EntityGraphType.LOAD)
  @Query("select m from Members m where m.fromSocial = :social and m.id= :id")
  Optional<Members> findById(String id, boolean social);

  @Query("select m from Members m where m.id = :id and m.password = :password")
  Optional<Members> findByIdAndPassword(@Param("id") String id, @Param("password") String pass);
}