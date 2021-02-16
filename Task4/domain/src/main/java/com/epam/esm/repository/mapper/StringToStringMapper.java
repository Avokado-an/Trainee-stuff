package com.epam.esm.repository.mapper;

import java.util.function.Function;

public class StringToStringMapper implements Function<String, String> {
    @Override
    public String apply(String s) {
        return s;
    }
}
