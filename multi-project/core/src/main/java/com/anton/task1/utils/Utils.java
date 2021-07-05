package com.anton.task1.utils;

public class Utils {
    public static boolean areAllPositiveNumbers(String... numbers) {
        boolean arePositive = true;
        for(String number: numbers) {
            if(!StringUtils.isPositiveNumber(number)) {
                arePositive = false;
            }
        }
        return arePositive;
    }
}
