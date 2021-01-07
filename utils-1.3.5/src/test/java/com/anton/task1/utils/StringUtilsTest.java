package com.anton.task1.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {
    @Test
    public void isPositiveNumberValidTest() {
        String positiveNumber = "1234";
        Assertions.assertTrue(StringUtils.isPositiveNumber(positiveNumber));
    }
}
