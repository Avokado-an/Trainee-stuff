package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.CreateGiftCertificateDto;
import com.epam.esm.model.dto.CreateTagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.impl.GiftCertificateServiceImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {
    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateService giftCertificateService;
    private TransactionManager transactionManager;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepository.class);
        transactionManager = mock(TransactionManager.class);
        giftCertificateService = new GiftCertificateServiceImplementation();
        modelMapper = new ModelMapper();
    }

    @AfterEach
    public void deactivate() {
        giftCertificateRepository = null;
        transactionManager = null;
        giftCertificateService = null;
        modelMapper = null;
    }

    @Test
    public void createCertificateValidTest() {
        String name = "qwer";
        String description = "qwer";
        long price = 100;
        int duration = 5;
        Set<CreateTagDto> tags = new HashSet<>();
        CreateGiftCertificateDto certificateDto = new CreateGiftCertificateDto(tags, name, description, price, duration);
        doNothing().when(transactionManager).createCertificate(any(GiftCertificate.class));
        //when(giftCertificateRepository.create(any(GiftCertificate.class))).thenReturn();
    }
}
