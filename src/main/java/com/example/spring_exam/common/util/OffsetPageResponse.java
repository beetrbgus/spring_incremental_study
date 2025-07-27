package com.example.spring_exam.common.util;

import lombok.Getter;

import java.util.List;

@Getter
public class OffsetPageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final boolean hasNext;
    private final Long totalCount;

    public OffsetPageResponse(List<T> content, int page, int size, boolean hasNext, Long totalCount) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
        this.totalCount = totalCount;
    }

}
