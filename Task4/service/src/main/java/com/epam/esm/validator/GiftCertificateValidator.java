package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class GiftCertificateValidator {
    private static final String FORBIDDEN_CHARACTERS = "<>?@#&";
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_PRICE = 100000;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_DURATION = 1000;
    private static final int MAX_TAG_AMOUNT = 1000;

    public boolean validateCertificate(GiftCertificate certificate) {
        return validateDescription(certificate.getDescription()) && validateDuration(certificate.getDuration())
                && validateName(certificate.getName()) && validatePrice(certificate.getPrice())
                && validateTagAmount(certificate.getTags().size());
    }

    public boolean validateName(String name) {
        boolean isValid = false;
        if (!isNull(name)) {
            isValid = name.length() > MIN_NAME_LENGTH
                    && name.length() < MAX_NAME_LENGTH && !name.contains(FORBIDDEN_CHARACTERS);
        }
        return isValid;
    }

    public boolean validateDescription(String description) {
        return description.length() > MIN_DESCRIPTION_LENGTH && description.length() < MAX_DESCRIPTION_LENGTH
                && !description.contains(FORBIDDEN_CHARACTERS);
    }

    public boolean validatePrice(long price) {
        return price > 0 && price < MAX_PRICE;
    }

    public boolean validateDuration(int duration) {
        return duration > 0 && duration < MAX_DURATION;
    }

    public boolean validatePrice(String price) {
        boolean isValidValue = false;
        if (!isNull(price)) {
            try {
                long priceValue = Long.parseLong(price);
                isValidValue = priceValue > 0 && priceValue < MAX_PRICE;
            } catch (NumberFormatException ignored) {

            }
        }
        return isValidValue;
    }

    public boolean validateDuration(String duration) {
        boolean isValidValue = false;
        if (!isNull(duration)) {
            try {
                long durationValue = Long.parseLong(duration);
                isValidValue = durationValue > 0 && durationValue < MAX_DURATION;
            } catch (NumberFormatException ignored) {

            }
        }
        return isValidValue;
    }

    public boolean validateTagAmount(int tagsAmount) {
        return tagsAmount < MAX_TAG_AMOUNT;
    }
}
