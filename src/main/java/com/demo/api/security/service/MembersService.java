package com.demo.api.security.service;

import com.demo.api.dto.MembersDTO;
import com.demo.api.entity.Members;
import com.demo.api.entity.MembersRole;
import com.demo.api.security.util.JWTUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface MembersService {
  default Members dtoToEntity(MembersDTO membersDTO) {
    Members members = Members.builder()
        .mno(membersDTO.getMno())
        .id(membersDTO.getId())
        .name(membersDTO.getName())
        .password(membersDTO.getPassword())
        .gender(membersDTO.getGender())
        .mobile(membersDTO.getMobile())
        .email(membersDTO.getEmail())
        .imgName(membersDTO.getImgName())
        .imgUuid(membersDTO.getImgUuid())
        .imgPath(membersDTO.getImgPath())
        .birthday(membersDTO.getBirthday())
        .fromSocial(membersDTO.isFromSocial())
        .roleSet(membersDTO.getRoleSet().stream().map(str -> {
          if (str.equals("ROLE_USER")) return MembersRole.USER;
          else if (str.equals("ROLE_MANAGER")) return MembersRole.MANAGER;
          else if (str.equals("ROLE_ADMIN")) return MembersRole.ADMIN;
          else return MembersRole.USER;
        }).collect(Collectors.toSet()))
        .build();
    return members;
  }

  default MembersDTO entityToDTO(Members members) {
    MembersDTO membersDTO = MembersDTO.builder()
        .mno(members.getMno())
        .id(members.getId())
        .password(members.getPassword())
        .name(members.getName())
        .email(members.getEmail())
        .gender(members.getGender())
        .mobile(members.getMobile())
        .imgName(members.getImgName())
        .imgUuid(members.getImgUuid())
        .imgPath(members.getImgPath())
        .fromSocial(members.isFromSocial())
        .regDate(members.getRegDate())
        .modDate(members.getModDate())
        .roleSet(members.getRoleSet().stream().map(
            new Function<MembersRole, String>() {
              @Override
              public String apply(MembersRole role) {
                return new String("ROLE_" + role.name());
              }
            }).collect(Collectors.toSet()))
        .build();
    return membersDTO;
  }

  Long registMembersDTO(MembersDTO membersDTO);

  void updateMembersDTO(MembersDTO membersDTO);

  void removeMembers(Long mno);

  MembersDTO get(Long num);

  List<MembersDTO> getAll();

  String login(String id, String pass, JWTUtil jwtUtil);
}
