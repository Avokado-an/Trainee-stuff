package com.anton.task1.utils;

public class StringUtils {
    public static boolean isPositiveNumber(String str) {
        return org.apache.commons.lang3.StringUtils.isNumeric(str);
    }
}
