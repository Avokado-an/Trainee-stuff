package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>?@#&";
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_PRICE = 100000;
    private static final int ZERO = 0;
    private static final int MAX_DURATION = 10000;
    private static final int MAX_TAG_AMOUNT = 1000;

    public boolean validateCertificate(GiftCertificate certificate) {
        return validateDescription(certificate.getDescription()) && validateDuration(certificate.getDuration())
                && validateName(certificate.getName()) && validatePrice(certificate.getPrice())
                && validateTagAmount(certificate.getTags().size());
    }

    private boolean validateName(String name) {
        return name.length() > ZERO && name.length() < MAX_NAME_LENGTH && !name.contains(FORBIDDEN_CHARACTERS);
    }

    private boolean validateDescription(String description) {
        return description.length() > ZERO && description.length() < MAX_DESCRIPTION_LENGTH
                && !description.contains(FORBIDDEN_CHARACTERS);
    }

    private boolean validatePrice(long price) {
        return price > ZERO && price < MAX_PRICE;
    }

    private boolean validateDuration(int duration) {
        return duration > ZERO && duration < MAX_DURATION;
    }

    private boolean validateTagAmount(int tagsAmount) {
        return tagsAmount < MAX_TAG_AMOUNT;
    }
}
