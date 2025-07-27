package com.example.spring_exam.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CursorPageResponse<T, C> {
    private List<T> content;
    private boolean hasNext;
    private C nextCursor;
}
