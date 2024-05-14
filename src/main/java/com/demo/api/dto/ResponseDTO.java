package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO { //응답 메시지와 결과를 포함하는 일반적인 응답 형식을 나타냄.
  //서버에서 클라이언트로 응답을 전달할 때 사용될 수 있음.
  private String msg;
  private boolean result;
}
