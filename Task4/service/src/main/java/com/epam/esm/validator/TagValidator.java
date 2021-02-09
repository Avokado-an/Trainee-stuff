package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.Objects.isNull;

@Component
public class TagValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>@#&";
    private static final int MAX_LENGTH = 150;
    private static final int MIN_LENGTH = 0;

    public boolean validateTag(Tag tag) {
        return validateName(tag.getName());
    }

    private boolean validateName(String tagName) {
        boolean isValid = false;
        if(!isNull(tagName)) {
            boolean doesContainForbiddenCharacters = Arrays.stream(tagName.split("")).anyMatch(FORBIDDEN_CHARACTERS::contains);
            isValid = tagName.length() > MIN_LENGTH && tagName.length() < MAX_LENGTH
                    && !doesContainForbiddenCharacters;
        }
        return isValid;
    }
}
