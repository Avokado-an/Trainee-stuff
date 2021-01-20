package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.internal.util.Assert;

public class TagValidatorTest {
    private static TagValidator tagValidator;

    @BeforeAll
    public static void setUp() {
        tagValidator = new TagValidator();
    }

    @Test
    public void validateTagValidTest() {
        Tag tag = new Tag();
        tag.setName("qwer");
        Assert.isTrue(tagValidator.validateTag(tag));
    }

    public static Object[][] validateTagIncorrectData() {
        return new Object[][]{
                {new Tag("qwer#<>#")},
                {new Tag("qwerffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")},
                {new Tag("")}
        };
    }

    @ParameterizedTest
    @MethodSource("validateTagIncorrectData")
    public void validateTagInvalidTest(Tag tag) {
        Assert.isTrue(!tagValidator.validateTag(tag));
    }
}
