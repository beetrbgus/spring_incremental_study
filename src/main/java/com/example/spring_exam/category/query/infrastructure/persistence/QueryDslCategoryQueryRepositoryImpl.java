package com.example.spring_exam.category.query.infrastructure.persistence;

import com.example.spring_exam.category.query.domain.CategoryReadModel;
import com.example.spring_exam.category.query.domain.QCategoryReadModel;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.spring_exam.category.command.domain.QCategory.category;

@Repository
@RequiredArgsConstructor
public class QueryDslCategoryQueryRepositoryImpl implements QueryDslCategoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryReadModel> getCategoryList() {
        List<CategoryReadModel> flatList = jpaQueryFactory
                .select(new QCategoryReadModel(
                        category.id,
                        category.name,
                        category.depth,
                        category.sortOrder,
                        category.isVisible,
                        category.isLeaf,
                        category.slug,
                        category.description,
                        category.parent.id,
                        null,
                        category.createdAt,
                        category.updatedAt
                ))
                .from(category)
                .fetch();

        return buildCategoryTree(flatList);
    }

    private List<CategoryReadModel> buildCategoryTree(List<CategoryReadModel> flatList) {
        Map<Long, CategoryReadModel> map = new HashMap<>();
        List<CategoryReadModel> roots = new ArrayList<>();

        for (CategoryReadModel node : flatList) {
            map.put(node.getId(), node);
        }

        for (CategoryReadModel node : flatList) {
            if (node.getParentId() != null) {
                CategoryReadModel parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            } else {
                roots.add(node);
            }
        }

        return roots;
    }
}
