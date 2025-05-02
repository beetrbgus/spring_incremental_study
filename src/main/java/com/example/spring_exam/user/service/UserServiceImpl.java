package com.example.spring_exam.user.service;

import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.dto.SignupReq;
import com.example.spring_exam.user.mapper.UserMapper;
import com.example.spring_exam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void signUp(SignupReq signupReq) {
        User user = userMapper.signupReqToEntity(signupReq);

        userRepository.save(user);
    }
}
