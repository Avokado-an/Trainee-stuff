package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TransactionManager {//todo create normal transactions
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
    public void createCertificate(GiftCertificate certificate) {
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        List<BigInteger> tagKeys = new ArrayList<>();
        BigInteger certificateKey;
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag)) {
                GeneratedKeyHolder currentKey = tagRepository.create(tag);
                tagKeys.add(BigInteger.valueOf(currentKey.getKey().longValue()));
            } else {
                Tag existingTag = existingTags.stream().filter(t -> t.getName().equals(tag.getName())).findFirst().get();
                tagKeys.add(BigInteger.valueOf(existingTag.getId()));
            }
        }
        certificateKey = BigInteger.valueOf(giftCertificateRepository.create(certificate).getKey().longValue());
        giftTagMappingRepository.createCertificate(certificateKey, tagKeys);
    }

    @Transactional
    public void updateCertificate(GiftCertificate certificate) {
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        Set<BigInteger> tagKeys = new HashSet<>();
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag)) {
                GeneratedKeyHolder currentTagKey = tagRepository.create(tag);
                tagKeys.add(BigInteger.valueOf(currentTagKey.getKey().longValue()));
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
        for(String tagName: tagNames) {
            Optional<Tag> tagToFind = tagRepository.readByName(tagName);
            if(tagToFind.isPresent()) {
                tagsId.add(BigInteger.valueOf(tagToFind.get().getId()));
            }
        }
        return tagsId;
    }
}
