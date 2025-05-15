package com.example.spring_exam.image.command.infrastucture.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslImageRepositoryImpl implements QueryDslImageRepository {
    private final JPAQueryFactory jpaQueryFactory;

}
