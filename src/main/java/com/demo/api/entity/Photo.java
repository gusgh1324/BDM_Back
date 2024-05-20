package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //uid  ===> uesr.mno와 연결


    private String title; //사진의 제목

    private String imageUrl; //사진의 URL 주소

    @Column(name = "result_text")//분석결과
    private String resultText;


    // 다른 필드들
}
