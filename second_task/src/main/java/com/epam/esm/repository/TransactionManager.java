package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

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

    public boolean createCertificate(GiftCertificate certificate) {//todo should I work with connection itself and disable auto commit until finish transaction
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        boolean wasCreated = true;
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag) && wasCreated) {
                wasCreated = tagRepository.create(tag);
            }
        }
        if (wasCreated) {
            wasCreated = giftCertificateRepository.create(certificate);
        }
        if (wasCreated) {
            wasCreated = giftTagMappingRepository.createCertificate(certificate);
        }
        return wasCreated;
    }

    public boolean updateCertificate(GiftCertificate certificate) {//todo should I work with connection itself and disable auto commit until finish transaction
        Set<Tag> existingTags = tagRepository.read();
        Set<Tag> requiredTags = certificate.getTags();
        boolean wasCreated = true;
        for (Tag tag : requiredTags) {
            if (!existingTags.contains(tag) && wasCreated) {
                wasCreated = tagRepository.create(tag);
            }
        }
        if (wasCreated) {
            wasCreated = giftTagMappingRepository.updateCertificate(certificate);
        }
        if (wasCreated) {
            giftCertificateRepository.update(certificate);
        }
        return wasCreated;
    }

    public boolean deleteTag(long index) {
        boolean wasDeleted = giftTagMappingRepository.deleteTag(index);
        if (wasDeleted) {
            wasDeleted = tagRepository.delete(index);
        }
        return wasDeleted;
    }

    public boolean deleteCertificate(long index) {
        boolean wasDeleted = giftTagMappingRepository.deleteCertificate(index);
        if (wasDeleted) {
            wasDeleted = giftCertificateRepository.delete(index);
        }
        return wasDeleted;
    }
}
