package com.example.spring_exam.common.util;

import java.util.Optional;

public class CursorPageRequest<C> implements PageRequestSupport {
    private C cursor;
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    public Optional<C> getCursor() {
        return Optional.ofNullable(cursor);
    }
}
