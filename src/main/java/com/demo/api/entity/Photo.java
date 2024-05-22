package com.demo.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mno")
    private User user;

    private String title; //사진의 제목

    private String imageUrl; //사진의 URL 주소

    // 사진id
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishDiseasePrediction> predictions = new ArrayList<>();


    // 다른 필드들
    public void addPrediction(FishDiseasePrediction prediction) {
        predictions.add(prediction);
        prediction.setPhoto(this);
    }

    public void removePrediction(FishDiseasePrediction prediction) {
        predictions.remove(prediction);
        prediction.setPhoto(null);
    }
}
