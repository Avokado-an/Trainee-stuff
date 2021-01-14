package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;

import java.util.HashSet;

public class GiftCertificateValidatorTest {
    private static GiftCertificateValidator giftCertificateValidator;
    private static ModelMapper modelMapper;

    @BeforeAll
    public static void setUp() {
        giftCertificateValidator = new GiftCertificateValidator();
        modelMapper = new ModelMapper();
    }

    @Test
    public void validateTagValidTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setTags(new HashSet<>());
        giftCertificate.setDescription("desc");
        giftCertificate.setName("qwer");
        giftCertificate.setDuration(5);
        giftCertificate.setPrice(10L);
        Assert.isTrue(giftCertificateValidator.validateCertificate(giftCertificate));
    }

    public static Object[][] validateGiftCertificateIncorrectData() {
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
    @MethodSource("validateGiftCertificateIncorrectData")
    public void validateTagInvalidTest(CreateGiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        Assert.isTrue(!giftCertificateValidator.validateCertificate(giftCertificate));
    }
}
