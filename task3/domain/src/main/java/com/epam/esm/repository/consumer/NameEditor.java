package com.epam.esm.repository.consumer;

import com.epam.esm.entity.GiftCertificate;

public class NameEditor implements FieldEditor<String, GiftCertificate> {
    @Override
    public void edit(String s, GiftCertificate certificate) {
        certificate.setName(s);
    }
}
