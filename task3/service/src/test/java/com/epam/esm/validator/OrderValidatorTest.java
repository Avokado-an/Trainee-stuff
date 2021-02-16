package com.epam.esm.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderValidatorTest {
    @TestConfiguration
    static class CertificateValidatorTestContextConfiguration {
        @Bean
        public OrderValidator orderValidator() {
            return new OrderValidator();
        }
    }

    @Autowired
    private OrderValidator orderValidator;

    @Test
    public void validateOrderValidTest() {
        long price = 100;
        Assert.assertTrue(orderValidator.validateOrder(price));
    }

    @Test
    public void validateOrderInvalidTest() {
        long price = -100;
        Assert.assertFalse(orderValidator.validateOrder(price));
    }
}
