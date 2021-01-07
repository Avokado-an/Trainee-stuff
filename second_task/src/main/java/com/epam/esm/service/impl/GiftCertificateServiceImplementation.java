package com.epam.esm.service.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.CertificateFilterDto;
import com.epam.esm.model.dto.CreateGiftCertificateDto;
import com.epam.esm.model.dto.UpdateGiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class GiftCertificateServiceImplementation implements GiftCertificateService {
    private GiftCertificateRepository giftCertificateRepository;
    private TransactionManager transactionManager;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setGiftCertificateRepository(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Set<GiftCertificate> create(CreateGiftCertificateDto giftCertificate) {
        GiftCertificate certificate = modelMapper.map(giftCertificate, GiftCertificate.class);
        certificate.setCreationDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        transactionManager.createCertificate(certificate);
        return giftCertificateRepository.read();
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
        if (certificate.isPresent()) {
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
    public Set<GiftCertificate> filter(CertificateFilterDto filterDto) {
        return null;
    }
}
