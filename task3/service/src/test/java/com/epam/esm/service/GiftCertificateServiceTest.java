package com.epam.esm.service;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImplementation;
import com.epam.esm.service.impl.OrderServiceImplementation;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.OrderValidator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class GiftCertificateServiceTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public GiftCertificateService orderService() {
            return new GiftCertificateServiceImplementation();
        }

        @Bean
        public GiftCertificateValidator giftCertificateValidator() {
            return new GiftCertificateValidator();
        }
    }

    @MockBean
    private GiftCertificateRepository giftCertificateRepository;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private GiftCertificateService giftCertificateService;

    @Autowired
    private GiftCertificateValidator giftCertificateValidator;
}
