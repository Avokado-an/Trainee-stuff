package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TagValidatorTest {
    @TestConfiguration
    static class CertificateValidatorTestContextConfiguration {
        @Bean
        public TagValidator tagValidator() {
            return new TagValidator();
        }
    }

    @Autowired
    private TagValidator tagValidator;

    @Test
    public void validateTagValidTest() {
        String tagName = "tag1";
        Tag tag = new Tag();
        tag.setName(tagName);
        Assert.assertTrue(tagValidator.validateTag(tag));
    }

    @Test
    public void validateTagInvalidTest() {
        String tagName = "tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1" +
                "tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1" +
                "tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1tag1";
        Tag tag = new Tag();
        tag.setName(tagName);
        Assert.assertFalse(tagValidator.validateTag(tag));
    }
}
