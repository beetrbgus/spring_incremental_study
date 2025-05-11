package com.example.spring_exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringExamApplication.class, args);
    }

}
