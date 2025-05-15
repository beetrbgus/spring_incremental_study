package com.example.spring_exam.image.command.domain;

import com.example.spring_exam.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "tb_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;

    @Enumerated(EnumType.STRING)
    private ImageType type;
    private String originFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    private Integer sortOrder;
    private Boolean isRepresentative;

    public static Image create(String originFileName,String url, User creator) {
        return Image.builder()
                .url(url)
                .originFileName(originFileName)
                .creator(creator)
                .build();
    }
}