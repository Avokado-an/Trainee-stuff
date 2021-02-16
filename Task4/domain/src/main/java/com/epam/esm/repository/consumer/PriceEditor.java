package com.epam.esm.repository.consumer;

import com.epam.esm.entity.GiftCertificate;

public class PriceEditor implements FieldEditor<Long, GiftCertificate> {
    @Override
    public void edit(Long l, GiftCertificate certificate) {
        certificate.setPrice(l);
    }
}
