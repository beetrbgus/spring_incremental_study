package com.example.spring_exam.image.command.infrastucture.persistence;

import com.example.spring_exam.image.command.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> ,QueryDslImageRepository {
}
