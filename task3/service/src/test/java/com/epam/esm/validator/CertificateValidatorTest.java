package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
public class CertificateValidatorTest {
    @TestConfiguration
    static class CertificateValidatorTestContextConfiguration {
        @Bean
        public GiftCertificateValidator giftCertificateValidator() {
            return new GiftCertificateValidator();
        }
    }

    @Autowired
    private GiftCertificateValidator giftCertificateValidator;

    @Test
    public void validateCertificateValidTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setTags(new HashSet<>());
        giftCertificate.setDescription("desc");
        giftCertificate.setName("qwer");
        giftCertificate.setDuration(5);
        giftCertificate.setPrice(10L);
        Assert.isTrue(giftCertificateValidator.validateCertificate(giftCertificate));
    }

    /*public static Stream<CreateGiftCertificateDto> giftCertificateIncorrectData() {
        return Stream.of(
                new CreateGiftCertificateDto(new HashSet<>(),
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
                ),
                new CreateGiftCertificateDto(new HashSet<>(), "qwer", "des", 40000000L, 5),
                new CreateGiftCertificateDto(new HashSet<>(), "<@@>>>>", "des", 4L, 50000000),
                new CreateGiftCertificateDto(new HashSet<>(), "qwer", "des", -1L, 5),
                new CreateGiftCertificateDto(new HashSet<>(), "<@@>>>>", "des", 4L, -1),
                new CreateGiftCertificateDto(new HashSet<>(), "", "des", 4L, 1),
                new CreateGiftCertificateDto(new HashSet<>(), "qqqq", "", 4L, -1)
        );
    }*/

    @Test
    public void validateCertificateInvalidTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .tags(new HashSet<>())
                .name("<@@>>>>")
                .description("<@@>>>>")
                .certificateOrders(new HashSet<>())
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .duration(-1)
                .id(1L)
                .price(4000000000L)
                .build();
        Assert.isTrue(!giftCertificateValidator.validateCertificate(giftCertificate));
    }

    @Test
    public void validateLongPriceValidTest() {
        long validPrice = 400L;
        Assert.isTrue(giftCertificateValidator.validatePrice(validPrice));
    }

    @Test
    public void validateLongPriceInvalidTest() {
        long invalidPrice = 4000000000000L;
        Assert.isTrue(!giftCertificateValidator.validatePrice(invalidPrice));
    }

    @Test
    public void validateStringPriceValidTest() {
        String validPrice = "400";
        Assert.isTrue(giftCertificateValidator.validatePrice(validPrice));
    }

    @Test
    public void validateStringPriceInvalidTest() {
        String invalidPrice = "4000000000000";
        Assert.isTrue(!giftCertificateValidator.validatePrice(invalidPrice));
    }

    @Test
    public void validateIntegerDurationValidTest() {
        int validDuration = 4;
        Assert.isTrue(giftCertificateValidator.validateDuration(validDuration));
    }

    @Test
    public void validateIntegerDurationInvalidTest() {
        int invalidDuration = 400000;
        Assert.isTrue(!giftCertificateValidator.validatePrice(invalidDuration));
    }

    @Test
    public void validateStringDurationValidTest() {
        String validDuration = "4";
        Assert.isTrue(giftCertificateValidator.validateDuration(validDuration));
    }

    @Test
    public void validateStringDurationInvalidTest() {
        String invalidDuration = "4000000000000";
        Assert.isTrue(!giftCertificateValidator.validateDuration(invalidDuration));
    }

    @Test
    public void validateNameValidTest() {
        String name = "qwerqwer";
        Assert.isTrue(giftCertificateValidator.validateName(name));
    }

    @Test
    public void validateNameInvalidTest() {
        String name = "qwerqwerrrrrrrrrrrrrrrrrrrrrrrrrrr" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr";
        Assert.isTrue(!giftCertificateValidator.validateName(name));
    }

    @Test
    public void validateDescriptionValidTest() {
        String name = "qwerqwer";
        Assert.isTrue(giftCertificateValidator.validateDescription(name));
    }
}
