package com.example.spring_exam.auth.service;

import com.example.spring_exam.common.exception.auth.CustomAuthException;
import com.example.spring_exam.common.response.ErrorCode;
import com.example.spring_exam.user.domain.User;
import com.example.spring_exam.user.domain.UserDetailsImpl;
import com.example.spring_exam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .filter(findUser -> findUser.isAccountNonLocked() & findUser.isAccountNonExpired() & findUser.isCredentialsNonExpired())
                .orElseThrow(() -> new CustomAuthException(ErrorCode.USER_NOT_FOUND));
        return new UserDetailsImpl(user);
    }
}
