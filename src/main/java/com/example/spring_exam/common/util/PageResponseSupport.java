package com.example.spring_exam.common.util;

import java.util.List;

public interface PageResponseSupport<T> {
    List<T> getContent();
    boolean hasNext();
}
