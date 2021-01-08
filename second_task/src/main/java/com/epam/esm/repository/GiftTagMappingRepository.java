package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void createCertificate(BigInteger certificateKey, List<BigInteger> tagKeys) {
        for (BigInteger currentKey : tagKeys) {
            jdbcTemplate.update(INSERT, certificateKey, currentKey);
        }
    }

    public boolean deleteTag(long tagId) {
        return jdbcTemplate.update(DELETE_TAG, tagId) > 0;
    }

    public boolean deleteCertificate(long certificateId) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, certificateId) > 0;
    }

    public boolean updateCertificate(GiftCertificate giftCertificate, Set<BigInteger> tagKeys) {
        boolean wasOperationSuccessful = true;
        Set<BigInteger> existingTags = new HashSet<>(
                jdbcTemplate.queryForList(SELECT_CERTIFICATE_TAGS, BigInteger.class, giftCertificate.getId()));
        for (BigInteger currentTagKey : tagKeys) {
            if (!existingTags.contains(currentTagKey)) {
                if (jdbcTemplate.update(INSERT, giftCertificate.getId(), currentTagKey) <= 0) {
                    wasOperationSuccessful = false;
                }
            }
        }
        for (BigInteger tagId : existingTags) {
            if (!tagKeys.contains(tagId)) {
                if (jdbcTemplate.update(DELETE_BY_CERTIFICATE_AND_TAG, giftCertificate.getId(), tagId) <= 0) {
                    wasOperationSuccessful = false;
                }
            }
        }
        return wasOperationSuccessful;
    }
}
