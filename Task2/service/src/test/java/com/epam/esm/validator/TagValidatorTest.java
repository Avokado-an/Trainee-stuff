package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;

public class TagValidatorTest {
    private TagValidator tagValidator;

    @BeforeAll
    public void setUp() {
        tagValidator = new TagValidator();
    }

    @Test
    public void validateTagValidTest() {
        Tag tag = new Tag();
        tag.setName("qwer");
        Assert.isTrue(tagValidator.validateTag(tag));
    }
}
