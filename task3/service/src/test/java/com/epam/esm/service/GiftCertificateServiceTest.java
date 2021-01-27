package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.UpdateGiftCertificateFieldDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.type.CertificateFieldsType;
import com.epam.esm.service.impl.GiftCertificateServiceImplementation;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GiftCertificateServiceTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public GiftCertificateService giftCertificateService() {
            return new GiftCertificateServiceImplementation();
        }

        @Bean
        public GiftCertificateValidator giftCertificateValidator() {
            return new GiftCertificateValidator();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
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

    @Test
    public void findByIdValidTest() {
        String id = "5";
        when(giftCertificateRepository.findById(any(Long.class))).thenReturn(Optional.of(new GiftCertificate()));
        Optional<GiftCertificate> certificate = giftCertificateService.findById(id);
        assertTrue(certificate.isPresent());
    }

    @Test
    public void findByIdInvalidTest() {
        String id = "5lll";
        Optional<GiftCertificate> certificate = giftCertificateService.findById(id);
        assertFalse(certificate.isPresent());
    }

    @Test
    public void deleteValidTest() {
        GiftCertificate certificate = new GiftCertificate();
        Set<GiftCertificate> certificates = new HashSet<>(Collections.singletonList(certificate));
        when(giftCertificateRepository.removeAllById(any(Long.class))).thenReturn(certificates.remove(certificate) ? 1 : 0);
        assertEquals(0, certificates.size());
    }

    @Test
    public void findAllValidTest() {
        when(giftCertificateRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<GiftCertificate> certificates = giftCertificateService.findAll(Pageable.unpaged());
        assertEquals(0, certificates.getTotalElements());
    }

    @Test
    public void updateFieldValidTest() {
        UpdateGiftCertificateFieldDto updatedField = new UpdateGiftCertificateFieldDto();
        updatedField.setCertificateId(1L);
        updatedField.setField(CertificateFieldsType.NAME);
        updatedField.setEditedValue("newName");
        Optional<GiftCertificate> certificate = Optional.of(GiftCertificate.builder().price(1L).duration(5)
                .description("qewr").name("qwer").tags(new HashSet<>()).name("name").build());
        when(giftCertificateRepository.findById(any(Long.class))).thenReturn(certificate);
        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(certificate.get());
        certificate = giftCertificateService.updateField(updatedField);
        assertEquals(certificate.get().getName(), updatedField.getEditedValue());
    }

    @Test
    public void updateFieldInvalidTest() {
        UpdateGiftCertificateFieldDto updatedField = new UpdateGiftCertificateFieldDto();
        updatedField.setCertificateId(1L);
        updatedField.setField(CertificateFieldsType.PRICE);
        updatedField.setEditedValue("neasdf");
        Optional<GiftCertificate> certificate = Optional.of(GiftCertificate.builder().price(1L).duration(5)
                .description("qewr").name("qwer").tags(new HashSet<>()).name("name").build());
        when(giftCertificateRepository.findById(any(Long.class))).thenReturn(certificate);
        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(certificate.get());
        certificate = giftCertificateService.updateField(updatedField);
        assertNotEquals(certificate.get().getPrice(), updatedField.getEditedValue());
    }

    @Test
    public void findIdsCertificatesValidTest() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(giftCertificateRepository.findById(any(Long.class))).thenReturn(Optional.of(new GiftCertificate()));
        List<GiftCertificate> certificates = giftCertificateService.findIdsCertificates(ids);
        assertEquals(2, certificates.size());
    }
}
