package com.epam.esm.service.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.CertificateFilterDto;
import com.epam.esm.model.dto.CreateGiftCertificateDto;
import com.epam.esm.model.dto.UpdateGiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GiftCertificateServiceImplementation implements GiftCertificateService {
    private GiftCertificateRepository giftCertificateRepository;
    private TransactionManager transactionManager;
    private GiftCertificateValidator giftCertificateValidator;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setGiftCertificateRepository(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Autowired
    public void setGiftCertificateValidator(GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Override
    public Optional<GiftCertificate> create(CreateGiftCertificateDto giftCertificate) {
        GiftCertificate certificate = modelMapper.map(giftCertificate, GiftCertificate.class);
        Optional<GiftCertificate> createdCertificate = Optional.empty();
        if (giftCertificateValidator.validateCertificate(certificate)) {
            certificate.setCreationDate(LocalDateTime.now());
            certificate.setLastUpdateDate(LocalDateTime.now());
            createdCertificate = Optional.of(transactionManager.createCertificate(certificate));
        }
        return createdCertificate;
    }

    @Override
    public Optional<GiftCertificate> findById(String id) {
        Optional<GiftCertificate> certificateToFind = Optional.empty();
        try {
            long certificateId = Long.parseLong(id);
            certificateToFind = giftCertificateRepository.read(certificateId);
        } catch (NumberFormatException ignored) {

        }
        return certificateToFind;
    }

    @Override
    public Set<GiftCertificate> findAll() {
        return giftCertificateRepository.read();
    }

    @Override
    public Optional<GiftCertificate> update(UpdateGiftCertificateDto updatedCertificate) {
        Optional<GiftCertificate> certificate = giftCertificateRepository.read(updatedCertificate.getId());
        if (certificate.isPresent() && giftCertificateValidator.validateCertificate(certificate.get())) {
            certificate.get().setLastUpdateDate(LocalDateTime.now());
            certificate.get().setDescription(updatedCertificate.getDescription());
            certificate.get().setDuration(updatedCertificate.getDuration());
            certificate.get().setName(updatedCertificate.getName());
            certificate.get().setPrice(updatedCertificate.getPrice());
            certificate.get().setLastUpdateDate(LocalDateTime.now());
            certificate.get().setTags(updatedCertificate.getTags());
            transactionManager.updateCertificate(certificate.get());
        }
        return certificate;
    }

    @Override
    public Set<GiftCertificate> delete(long id) {
        transactionManager.deleteCertificate(id);
        return giftCertificateRepository.read();
    }

    @Override
    public List<GiftCertificate> filter(CertificateFilterDto filterDto) {
        return giftCertificateRepository.filterCertificates(filterDto);
    }
}
