package com.example.spring_exam.category.command.infrastucture.persistence;

import com.example.spring_exam.category.command.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryCommandRepository extends JpaRepository<Category, Long> , QueryDslCategoryRepository {
    boolean existsByName(String name);
}
