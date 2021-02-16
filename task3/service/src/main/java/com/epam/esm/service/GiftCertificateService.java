package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateFieldDto;
import com.epam.esm.entity.dto.representation.GiftCertificateRepresentationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateService {
    Optional<GiftCertificateRepresentationDto> create(CreateGiftCertificateDto giftCertificate);

    Optional<GiftCertificateRepresentationDto> findById(String id);

    Page<GiftCertificateRepresentationDto> findAll(Pageable pageable);

    Optional<GiftCertificateRepresentationDto> update(UpdateGiftCertificateDto newCertificate);

    Set<GiftCertificateRepresentationDto> delete(long id);

    List<GiftCertificateRepresentationDto> filter(CertificateFilterDto filterDto);

    Optional<GiftCertificateRepresentationDto> updateField(UpdateGiftCertificateFieldDto updatedField);

    List<GiftCertificate> findIdsCertificates(List<Long> certificatesId);
}
