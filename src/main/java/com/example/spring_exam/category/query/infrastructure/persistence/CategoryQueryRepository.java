package com.example.spring_exam.category.query.infrastructure.persistence;

import com.example.spring_exam.category.command.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryQueryRepository extends JpaRepository<Category, Long>, QueryDslCategoryQueryRepository {

}