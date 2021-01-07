package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GiftTagMappingRepository {
    private static final String INSERT = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";
    private static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    private static final String DELETE_BY_CERTIFICATE_AND_TAG =
            "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    private static final String SELECT_CERTIFICATE_TAGS =
            "SELECT tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftTagMappingRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean createCertificate(GiftCertificate giftCertificate) {
        boolean wasOperationValid = true;
        Set<Tag> certificateTags = giftCertificate.getTags();
        for (Tag tag : certificateTags) {
            if (jdbcTemplate.update(INSERT, giftCertificate.getId(), tag.getId()) <= 0) {
                wasOperationValid = false;
            }
        }
        return wasOperationValid;
    }

    public boolean deleteTag(long tagId) {
        return jdbcTemplate.update(DELETE_TAG, tagId) > 0;
    }

    public boolean deleteCertificate(long certificateId) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, certificateId) > 0;
    }

    public boolean updateCertificate(GiftCertificate giftCertificate) {
        boolean wasOperationSuccessful = true;
        Set<Long> updatedTags = giftCertificate.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<Long> existingTags = new HashSet<>(jdbcTemplate.queryForList(SELECT_CERTIFICATE_TAGS, Long.class, giftCertificate.getId()));
        for (Long tagId : updatedTags) {
            if (!existingTags.contains(tagId) && wasOperationSuccessful) {
                if (jdbcTemplate.update(INSERT, giftCertificate.getId(), tagId) <= 0) {
                    wasOperationSuccessful = false;
                }
            }
        }
        for (Long tagId : existingTags) {
            if (!updatedTags.contains(tagId) && wasOperationSuccessful) {
                if (jdbcTemplate.update(DELETE_BY_CERTIFICATE_AND_TAG, giftCertificate.getId(), tagId) <= 0) {
                    wasOperationSuccessful = false;
                }
            }
        }
        return wasOperationSuccessful;
    }
}
