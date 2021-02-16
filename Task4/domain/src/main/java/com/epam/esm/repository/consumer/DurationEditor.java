package com.epam.esm.repository.consumer;

import com.epam.esm.entity.GiftCertificate;

public class DurationEditor implements FieldEditor<Integer, GiftCertificate> {
    @Override
    public void edit(Integer i, GiftCertificate certificate) {
        certificate.setDuration(i);
    }
}
