package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import com.epam.esm.repository.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class GiftCertificateRepository implements CrudRepository<GiftCertificate> {
    private static final String INSERT = "INSERT INTO gift_certificate " +
            "(name, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE gift_certificate SET name = ?, description = ?, price = ?, " +
            "duration = ?, last_update_date = ? WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM gift_certificate";
    private static final String SELECT_BY_INDEX = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_CERTIFICATE_TAGS = "SELECT * FROM tag " +
            "INNER JOIN gift_certificate_tag ON gift_certificate_tag.tag_id = tag.id " +
            "WHERE gift_certificate_tag.gift_certificate_id = ?";
    private static final String REMOVE_BY_INDEX = "DELETE FROM gift_certificate WHERE id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate giftCertificate) {
        Object[] args = new Object[]{giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getLastUpdateDate(),
                giftCertificate.getId()};
        int updatedCount = jdbcTemplate.update(UPDATE, args);
        return updatedCount > 0 ? Optional.of(giftCertificate) : Optional.empty();
    }

    @Override
    public boolean create(GiftCertificate giftCertificate) {
        Object[] args = new Object[]{giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreationDate(),
                giftCertificate.getLastUpdateDate()};
        int createdCertificatesCount = jdbcTemplate.update(INSERT, args);
        return createdCertificatesCount == 1;
    }

    @Override
    public Optional<GiftCertificate> read(long index) { //todo specify what exactly happens when you get nothing(exception ot null)
        Optional<GiftCertificate> resultWrapper = Optional.empty();
        try {
            GiftCertificate certificate =
                    jdbcTemplate.queryForObject(SELECT_BY_INDEX, new Object[]{index}, new GiftCertificateMapper());
            if (certificate != null) {
                Set<Tag> certificateTags =
                        new HashSet<>(jdbcTemplate.query(SELECT_CERTIFICATE_TAGS, new TagMapper(), certificate.getId()));
                certificate.setTags(certificateTags);
                resultWrapper = Optional.of(certificate);
            }
        } catch (EmptyResultDataAccessException ignored) {

        }
        return resultWrapper;
    }

    @Override
    public Set<GiftCertificate> read() {
        Set<GiftCertificate> certificates = new HashSet<>(jdbcTemplate.query(SELECT_ALL, new GiftCertificateMapper()));
        for (GiftCertificate certificate : certificates) {
            Set<Tag> certificateTags =
                    new HashSet<>(jdbcTemplate.query(SELECT_CERTIFICATE_TAGS, new TagMapper(), certificate.getId()));
            certificate.setTags(certificateTags);
        }
        return certificates;
    }

    @Override
    public boolean delete(long index) {
        return jdbcTemplate.update(REMOVE_BY_INDEX, index) > 0;
    }
}
