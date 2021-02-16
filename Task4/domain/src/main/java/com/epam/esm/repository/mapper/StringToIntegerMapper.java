package com.epam.esm.repository.mapper;

import java.util.function.Function;

public class StringToIntegerMapper implements Function<String, Integer> {
    @Override
    public Integer apply(String s) {
        return Integer.parseInt(s);
    }
}
