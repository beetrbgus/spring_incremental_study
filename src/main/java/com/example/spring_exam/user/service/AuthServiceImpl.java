package com.example.spring_exam.user.service;

import com.example.spring_exam.user.dto.SignupReq;
import com.example.spring_exam.user.repository.AuthRepository;
import com.example.spring_exam.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public void signUp(SignupReq signupReq) {
        User user = User.createMember(signupReq);
        authRepository.save(user);
    }
}
