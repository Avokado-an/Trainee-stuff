package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.type.SortType;
import com.epam.esm.service.specification.GiftCertificateSpecification;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.entity.dto.CreateGiftCertificateDto;
import com.epam.esm.entity.dto.UpdateGiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.type.SearchType;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GiftCertificateServiceImplementation implements GiftCertificateService {
    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateValidator giftCertificateValidator;
    private TagRepository tagRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setGiftCertificateRepository(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setGiftCertificateValidator(GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> create(CreateGiftCertificateDto giftCertificate) {
        GiftCertificate certificate = modelMapper.map(giftCertificate, GiftCertificate.class);
        Optional<GiftCertificate> createdCertificate = Optional.empty();
        if (giftCertificateValidator.validateCertificate(certificate)) {
            certificate.setCreationDate(LocalDateTime.now());
            certificate.setLastUpdateDate(LocalDateTime.now());
            List<Tag> existingTags = tagRepository.findAll();
            Set<Tag> requiredTags = certificate.getTags();
            Set<Tag> certificateTags = new HashSet<>();
            for (Tag tag : requiredTags) {
                if (!existingTags.contains(tag)) {
                    tagRepository.save(tag);
                }
                certificateTags.add(tagRepository.findByName(tag.getName()));
            }
            certificate.setTags(certificateTags);
            createdCertificate = Optional.of(giftCertificateRepository.save(certificate));
        }
        return createdCertificate;
    }

    @Override
    public Optional<GiftCertificate> findById(String id) {
        Optional<GiftCertificate> certificateToFind = Optional.empty();
        try {
            long certificateId = Long.parseLong(id);
            certificateToFind = giftCertificateRepository.findById(certificateId);
        } catch (NumberFormatException ignored) {

        }
        return certificateToFind;
    }

    @Override
    public Set<GiftCertificate> findAll() {
        return new HashSet<>(giftCertificateRepository.findAll());
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> update(UpdateGiftCertificateDto updatedCertificate) {
        Optional<GiftCertificate> certificate = giftCertificateRepository.findById(updatedCertificate.getId());
        if (certificate.isPresent() && giftCertificateValidator.validateCertificate(certificate.get())) {
            certificate.get().setLastUpdateDate(LocalDateTime.now());
            certificate.get().setDescription(updatedCertificate.getDescription());
            certificate.get().setDuration(updatedCertificate.getDuration());
            certificate.get().setName(updatedCertificate.getName());
            certificate.get().setPrice(updatedCertificate.getPrice());
            certificate.get().setLastUpdateDate(LocalDateTime.now());
            Set<Tag> certificateTagsFromDb = updateCertificateTags(updatedCertificate.getTags());
            certificate.get().setTags(certificateTagsFromDb);
            giftCertificateRepository.save(certificate.get());
        }
        return certificate;
    }

    @Transactional
    @Override
    public Set<GiftCertificate> delete(long id) {
        giftCertificateRepository.removeAllById(id);
        return new HashSet<>(giftCertificateRepository.findAll());
    }

    @Override
    public List<GiftCertificate> filter(CertificateFilterDto filter) {
        List<GiftCertificate> filteredCertificates;
        Optional<Specification<GiftCertificate>> currentSpecification = defineSpecification(
                filter.getSearchTypes(), filter.getTagName(), filter.getCertificateNameOrDescription());
        Sort sortType = defineSortType(filter.getSortTypes());
        if(currentSpecification.isPresent()) {
            filteredCertificates = giftCertificateRepository.findAll(currentSpecification.get(), sortType);
        } else {
            filteredCertificates = giftCertificateRepository.findAll(sortType);
        }
        return filteredCertificates;
    }

    private Optional<Specification<GiftCertificate>> defineSpecification(List<SearchType> searchTypes, String tagName, String name) {
        Optional<Specification<GiftCertificate>> currentSpecification = Optional.empty();
        if (searchTypes.contains(SearchType.SEARCH_BY_NAME_OR_DESCRIPTION_BEGINNING)
                && searchTypes.contains(SearchType.SEARCH_BY_TAG)) {
            currentSpecification = Optional.of(Specification.where(GiftCertificateSpecification
                    .filterCertificatesByTag(tagName)).and(Specification.where(GiftCertificateSpecification
                    .filterCertificatesByNameDescriptionStart(name))));
        } else if (searchTypes.contains(SearchType.SEARCH_BY_NAME_OR_DESCRIPTION_BEGINNING)) {
            currentSpecification = Optional.of(Specification.where(GiftCertificateSpecification
                    .filterCertificatesByNameDescriptionStart(name)));
        } else if (searchTypes.contains(SearchType.SEARCH_BY_TAG)) {
            currentSpecification = Optional.of(Specification.where(GiftCertificateSpecification
                    .filterCertificatesByTag(tagName)));
        }
        return currentSpecification;
    }

    private Sort defineSortType(List<SortType> sortTypes) {
        Sort sortAlgorithm;
        if(!sortTypes.isEmpty()) {
            sortAlgorithm = sortTypes.get(0).getSort();
            for(int i = 1; i < sortTypes.size(); i++) {
                sortAlgorithm = sortAlgorithm.and(sortTypes.get(i).getSort());
            }
        } else {
            sortAlgorithm = SortType.DEFAULT.getSort();
        }
        return sortAlgorithm;
    }

    private Set<Tag> updateCertificateTags(Set<Tag> requiredTags) {
        List<Tag> existingTags = tagRepository.findAll();
        Set<Tag> certificateTags = new HashSet<>();
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag)) {
                tagRepository.save(tag);
            }
        }
        for(Tag tag: requiredTags) {
            certificateTags.add(tagRepository.findByName(tag.getName()));
        }
        return certificateTags;
    }
}
