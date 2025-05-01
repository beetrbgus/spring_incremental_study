package com.example.spring_exam.user.domain;

import com.example.spring_exam.user.dto.SignupReq;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tb_user")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static User createMember(SignupReq signupReq) {
        return User.builder()
                .username(signupReq.username())
                .password(signupReq.password())
                .email(signupReq.email())
                .nickname(signupReq.nickname())
                .role(UserRole.USER)
                .build();
    }
}
