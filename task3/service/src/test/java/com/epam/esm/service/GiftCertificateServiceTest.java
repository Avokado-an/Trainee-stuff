package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import com.epam.esm.entity.dto.CreateTagDto;
import com.epam.esm.service.impl.GiftCertificateServiceImplementation;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {
    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateServiceImplementation giftCertificateService;
    private TransactionManager transactionManager;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepository.class);
        transactionManager = mock(TransactionManager.class);
        giftCertificateService = new GiftCertificateServiceImplementation();
        giftCertificateService.setGiftCertificateValidator(new GiftCertificateValidator());
        giftCertificateService.setTransactionManager(transactionManager);
        giftCertificateService.setGiftCertificateRepository(giftCertificateRepository);
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
        when(transactionManager.createCertificate(any(GiftCertificate.class)))
                .thenReturn(modelMapper.map(certificateDto, GiftCertificate.class));
        Optional<GiftCertificate> actual = Optional.of(modelMapper.map(certificateDto, GiftCertificate.class));
        Optional<GiftCertificate> expected = giftCertificateService.create(certificateDto);
        assertEquals(actual, expected);
    }

    public static Object[][] createGiftCertificateIncorrectData() {
        return new Object[][]{
                {new CreateGiftCertificateDto(new HashSet<>(),
                        "qwerfffffffffffffffffffffffffffffffffffff" +
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        "des", 4L, 5
                )},
                {new CreateGiftCertificateDto(new HashSet<>(), "qwer", "des", 40000000L, 5)},
                {new CreateGiftCertificateDto(new HashSet<>(), "<@@>>>>", "des", 4L, 50000000)},
                {new CreateGiftCertificateDto(new HashSet<>(), "qwer", "des", -1, 5)},
                {new CreateGiftCertificateDto(new HashSet<>(), "<@@>>>>", "des", 4L, -1)},
                {new CreateGiftCertificateDto(new HashSet<>(), "", "des", 4L, 1)},
                {new CreateGiftCertificateDto(new HashSet<>(), "qqqq", "", 4L, -1)},
        };
    }

    @ParameterizedTest
    @MethodSource("createGiftCertificateIncorrectData")
    public void createCertificateInvalidTest(CreateGiftCertificateDto certificateDto) {
        Optional<GiftCertificate> actual = Optional.empty();
        Optional<GiftCertificate> expected = giftCertificateService.create(certificateDto);
        assertEquals(actual, expected);
    }

    @Test
    public void readCertificateByIdValidTest() {
        long id = 5L;
        String name = "qwer";
        String description = "qwer";
        long price = 100;
        int duration = 5;
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        Set<Tag> tags = new HashSet<>();
        GiftCertificate giftCertificate =
                new GiftCertificate(id, name, description, tags, price, duration, creationTime, updateTime);
        when(giftCertificateRepository.read(any(Long.class)))
                .thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> actual = Optional.of(giftCertificate);
        Optional<GiftCertificate> expected = giftCertificateService.findById(Long.toString(id));
        assertEquals(actual, expected);
    }

    @Test
    public void readCertificateByIdInvalidTest() {
        String id = "qwer";
        Optional<GiftCertificate> expected = giftCertificateService.findById(id);
        assertFalse(expected.isPresent());
    }

    @Test
    public void readCertificateValidTest() {
        int actualSize = 1;
        Set<GiftCertificate> actualCertificates = new HashSet<>();
        actualCertificates.add(new GiftCertificate());
        when(giftCertificateRepository.read())
                .thenReturn(actualCertificates);
        Set<GiftCertificate> expectedCertificates = giftCertificateService.findAll();
        int expectedSize = expectedCertificates.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void deleteCertificateValidTest() {
        Set<GiftCertificate> certificates = new HashSet<>();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        certificates.add(certificate);
        doNothing().when(transactionManager).deleteTag(any(Long.class));
        giftCertificateService.delete(1L);
        int unchangedSize = 1;
        assertEquals(certificates.size(), unchangedSize);
    }
}
