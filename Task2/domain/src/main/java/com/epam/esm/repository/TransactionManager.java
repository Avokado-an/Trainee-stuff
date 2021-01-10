package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TransactionManager {
    private TagRepository tagRepository;
    private GiftCertificateRepository giftCertificateRepository;
    private GiftTagMappingRepository giftTagMappingRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setGiftCertificateRepository(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setGiftTagMappingRepository(GiftTagMappingRepository giftTagMappingRepository) {
        this.giftTagMappingRepository = giftTagMappingRepository;
    }

    @Transactional
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        List<BigInteger> tagKeys = new ArrayList<>();
        BigInteger certificateKey;
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag)) {
                Tag currentTag = tagRepository.create(tag);
                tagKeys.add(BigInteger.valueOf(currentTag.getId()));
            } else {
                Tag existingTag = existingTags.stream().filter(t -> t.getName().equals(tag.getName())).findFirst().get();
                tagKeys.add(BigInteger.valueOf(existingTag.getId()));
            }
        }
        GiftCertificate createdCertificate = giftCertificateRepository.create(certificate);
        certificateKey = BigInteger.valueOf(createdCertificate.getId());
        giftTagMappingRepository.createCertificate(certificateKey, tagKeys);
        return createdCertificate;
    }

    @Transactional
    public void updateCertificate(GiftCertificate certificate) {
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        Set<BigInteger> tagKeys = new HashSet<>();
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag)) {
                Tag currentTag = tagRepository.create(tag);
                tagKeys.add(BigInteger.valueOf(currentTag.getId()));
            }
        }
        tagKeys.addAll(findUpdatedCertificateTagsId(certificate));
        Optional<GiftCertificate> updatedCertificate = giftCertificateRepository.update(certificate);
        updatedCertificate.ifPresent(giftCertificate ->
                giftTagMappingRepository.updateCertificate(giftCertificate, tagKeys));
    }

    @Transactional
    public void deleteTag(long index) {
        giftTagMappingRepository.deleteTag(index);
        tagRepository.delete(index);
    }

    @Transactional
    public void deleteCertificate(long index) {
        giftTagMappingRepository.deleteCertificate(index);
        giftCertificateRepository.delete(index);
    }

    private List<BigInteger> findUpdatedCertificateTagsId(GiftCertificate certificate) {
        Set<String> tagNames = certificate.getTags().stream().map(t -> t.getName()).collect(Collectors.toSet());
        List<BigInteger> tagsId = new ArrayList<>();
        for (String tagName : tagNames) {
            Optional<Tag> tagToFind = tagRepository.readByName(tagName);
            if (tagToFind.isPresent()) {
                tagsId.add(BigInteger.valueOf(tagToFind.get().getId()));
            }
        }
        return tagsId;
    }
}
