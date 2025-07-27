package com.example.spring_exam.common.util;

import java.util.List;
import java.util.function.Function;

public class PageUtils {

    public static <T> OffsetPageResponse<T> toOffsetPage(List<T> data, int page, int size, Long totalCount) {
        boolean hasNext = data.size() > size;
        List<T> content = hasNext ? data.subList(0, size) : data;

        return new OffsetPageResponse<>(content, page, size, hasNext, totalCount);
    }

    public static <T, C> CursorPageResponse<T, C> toCursorPage(List<T> data, int size, Function<T, C> cursorExtractor) {
        boolean hasNext = data.size() > size;
        List<T> content = hasNext ? data.subList(0, size) : data;
        C nextCursor = hasNext ? cursorExtractor.apply(content.get(content.size() - 1)) : null;

        return new CursorPageResponse<>(content, hasNext, nextCursor);
    }
}
