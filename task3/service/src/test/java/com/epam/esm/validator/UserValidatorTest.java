package com.epam.esm.validator;

import com.epam.esm.entity.dto.CreateUserDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserValidatorTest {
    @TestConfiguration
    static class CertificateValidatorTestContextConfiguration {
        @Bean
        public UserValidator userValidator() {
            return new UserValidator();
        }
    }

    @Autowired
    private UserValidator userValidator;

    @Test
    public void validateUserValidTest() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("user");
        user.setBirthday(LocalDateTime.parse("2000-01-14T17:03:22"));
        Assert.assertTrue(userValidator.validateUser(user));
    }

    @Test
    public void validateUserInvalidTest() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("user$$$");
        user.setBirthday(LocalDateTime.parse("1000-01-14T17:03:22"));
        Assert.assertFalse(userValidator.validateUser(user));
    }
}
