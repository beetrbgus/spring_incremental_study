package com.example.spring_exam.user.repository;

import com.example.spring_exam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
