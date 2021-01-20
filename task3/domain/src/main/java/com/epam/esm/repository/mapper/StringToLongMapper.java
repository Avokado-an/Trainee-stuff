package com.epam.esm.repository.mapper;

import java.util.function.Function;

public class StringToLongMapper implements Function<String, Long> {
    @Override
    public Long apply(String s) {
        return Long.parseLong(s);
    }
}
