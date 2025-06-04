package com.xxxiv.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageableNormalizer {
    private static final int MAX_PAGE_SIZE = 50;

    public Pageable normalize(Pageable p) {
        int size = Math.min(p.getPageSize(), MAX_PAGE_SIZE);
        return PageRequest.of(p.getPageNumber(), size, p.getSort());
    }
}

