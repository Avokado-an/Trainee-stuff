package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.CertificateFilterDto;
import com.epam.esm.model.dto.CreateGiftCertificateDto;
import com.epam.esm.model.dto.UpdateGiftCertificateDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateService {
    Optional<GiftCertificate> create(CreateGiftCertificateDto giftCertificate);

    Optional<GiftCertificate> findById(String id);

    Set<GiftCertificate> findAll();

    Optional<GiftCertificate> update(UpdateGiftCertificateDto newCertificate);

    Set<GiftCertificate> delete(long id);

    List<GiftCertificate> filter(CertificateFilterDto filterDto);
}
