package com.example.spring_exam.common.util;

public class OffsetPageRequest<C> implements PageRequestSupport {
    private int page;
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    public int getOffset() {
        return page * size;
    }
}
