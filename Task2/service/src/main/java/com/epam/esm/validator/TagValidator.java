package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>@#&";
    private static final int MAX_LENGTH = 150;
    private static final int MIN_LENGTH = 0;

    public boolean validateTag(Tag tag) {
        return validateName(tag.getName());
    }

    private boolean validateName(String tagName) {
        return tagName.length() > MIN_LENGTH && tagName.length() < MAX_LENGTH
                && !tagName.contains(FORBIDDEN_CHARACTERS);
    }
}
