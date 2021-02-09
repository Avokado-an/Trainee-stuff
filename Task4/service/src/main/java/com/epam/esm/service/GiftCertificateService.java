package com.epam.esm.service;

import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.CreateGiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.dto.UpdateGiftCertificateFieldDto;
import com.epam.esm.dto.representation.GiftCertificateRepresentationDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResultNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateService {
    GiftCertificateRepresentationDto create(CreateGiftCertificateDto giftCertificate);

    GiftCertificateRepresentationDto findById(String id) throws ResultNotFoundException;

    Page<GiftCertificateRepresentationDto> findAll(Pageable pageable);

    Optional<GiftCertificateRepresentationDto> update(UpdateGiftCertificateDto newCertificate);

    Set<GiftCertificateRepresentationDto> delete(long id);

    List<GiftCertificateRepresentationDto> filter(CertificateFilterDto filterDto);

    Optional<GiftCertificateRepresentationDto> updateField(UpdateGiftCertificateFieldDto updatedField);

    List<GiftCertificate> findIdsCertificates(List<Long> certificatesId);
}
