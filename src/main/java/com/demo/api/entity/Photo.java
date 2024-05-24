package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    private String fileUrl;
    private String analysisResult;

    // @ManyToOne 애너테이션과 @JoinColumn 애너테이션을 사용하여 외래 키를 설정합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "mno", insertable = false, updatable = false)
    private User user;

}
