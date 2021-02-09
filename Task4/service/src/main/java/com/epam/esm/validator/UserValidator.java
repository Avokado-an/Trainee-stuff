package com.epam.esm.validator;

import com.epam.esm.dto.CreateUserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Component
public class UserValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>?@#&";
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final LocalDateTime MAX_BIRTHDAY_DATE = LocalDateTime.now().minusYears(3);
    private static final LocalDateTime MIN_BIRTHDAY_DATE = LocalDateTime.now().minusYears(110);

    public boolean validateUser(CreateUserDto newUser) {
        return validateName(newUser.getUsername()) && validateBirthday(newUser.getBirthday())
                && validatePassword(newUser.getPassword());
    }

    private boolean validateName(String name) {
        boolean isValid = false;
        if (!isNull(name)) {
            isValid = name.length() > MIN_NAME_LENGTH
                    && name.length() < MAX_NAME_LENGTH && !name.contains(FORBIDDEN_CHARACTERS);
        }
        return isValid;
    }

    private boolean validateBirthday(LocalDateTime birthDay) {
        boolean isValid = false;
        if (!isNull(birthDay)) {
            isValid = birthDay.compareTo(MAX_BIRTHDAY_DATE) <= 0 && birthDay.compareTo(MIN_BIRTHDAY_DATE) > 0;
        }
        return isValid;
    }

    private boolean validatePassword(String password) {
        boolean isValid = false;
        if (!isNull(password)) {
            isValid = password.length() > MIN_PASSWORD_LENGTH && password.length() < MAX_PASSWORD_LENGTH;
        }
        return isValid;
    }
}
