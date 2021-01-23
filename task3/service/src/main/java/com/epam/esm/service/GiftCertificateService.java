package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateFieldDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateService {
    Optional<GiftCertificate> create(CreateGiftCertificateDto giftCertificate);

    Optional<GiftCertificate> findById(String id);

    Optional<GiftCertificate> findById(long id);

    Page<GiftCertificate> findAll(Pageable pageable);

    Optional<GiftCertificate> update(UpdateGiftCertificateDto newCertificate);

    Set<GiftCertificate> delete(long id);

    List<GiftCertificate> filter(CertificateFilterDto filterDto);

    Optional<GiftCertificate> updateField(UpdateGiftCertificateFieldDto updatedField);

    List<GiftCertificate> findIdsCertificates(List<Long> certificatesId);
}
