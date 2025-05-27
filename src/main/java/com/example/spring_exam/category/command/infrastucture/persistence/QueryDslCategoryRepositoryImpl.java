package com.example.spring_exam.category.command.infrastucture.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslCategoryRepositoryImpl implements QueryDslCategoryRepository {
    private final JPAQueryFactory jpaQueryFactory;

}
