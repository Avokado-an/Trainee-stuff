package com.epam.esm.validator;

import com.epam.esm.entity.dto.CreateUserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>?@#&";
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 3;
    private static final LocalDateTime MAX_BIRTHDAY_DATE = LocalDateTime.now().minusYears(3);
    private static final LocalDateTime MIN_BIRTHDAY_DATE = LocalDateTime.now().minusYears(110);

    public boolean validateUser(CreateUserDto newUser) {
        return validateName(newUser.getUsername()) && validateBirthday(newUser.getBirthday());
    }

    private boolean validateName(String name) {
        return name.length() > MIN_NAME_LENGTH && name.length() < MAX_NAME_LENGTH && !name.contains(FORBIDDEN_CHARACTERS);
    }

    private boolean validateBirthday(LocalDateTime birthDay) {
        return birthDay.compareTo(MAX_BIRTHDAY_DATE) <= 0 && birthDay.compareTo(MIN_BIRTHDAY_DATE) > 0;
    }
}
