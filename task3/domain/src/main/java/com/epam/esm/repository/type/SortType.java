package com.epam.esm.repository.type;

import org.springframework.data.domain.Sort;

public enum SortType {
    BY_NAME_ASC(Sort.by(Sort.Direction.ASC, "name")),
    BY_NAME_DESC(Sort.by(Sort.Direction.DESC, "name")),
    BY_DATE_ASC(Sort.by(Sort.Direction.ASC, "creationDate")),
    BY_DATE_DESC(Sort.by(Sort.Direction.DESC, "creationDate")),
    DEFAULT(Sort.by(Sort.DEFAULT_DIRECTION, "id"));

    private Sort sort;

    public Sort getSort() {
        return sort;
    }

    SortType(Sort sort) {
        this.sort = sort;
    }
}
