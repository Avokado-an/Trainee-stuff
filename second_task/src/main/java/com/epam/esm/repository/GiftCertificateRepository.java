package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
    private static final String REMOVE_BY_INDEX = "DELETE FROM gift_certificate WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private TagRepository tagRepository;

    @Autowired
    public GiftCertificateRepository(DataSource dataSource, TagRepository tagRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.tagRepository = tagRepository;
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
    public GeneratedKeyHolder create(GiftCertificate giftCertificate) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setLong(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setObject(5, giftCertificate.getCreationDate());
            preparedStatement.setObject(6, giftCertificate.getLastUpdateDate());
            return preparedStatement;
        }, keyHolder);
        return keyHolder;
    }

    @Override
    public Optional<GiftCertificate> read(long index) { //todo specify what exactly happens when you get nothing(exception ot null)
        Optional<GiftCertificate> resultWrapper = Optional.empty();
        try {
            GiftCertificate certificate =
                    jdbcTemplate.queryForObject(SELECT_BY_INDEX, new Object[]{index}, new GiftCertificateMapper());
            if (certificate != null) {
                Set<Tag> certificateTags = tagRepository.readCertificateTags(certificate.getId());
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
            Set<Tag> certificateTags = tagRepository.readCertificateTags(certificate.getId());
            certificate.setTags(certificateTags);
        }
        return certificates;
    }

    @Override
    public boolean delete(long index) {
        return jdbcTemplate.update(REMOVE_BY_INDEX, index) > 0;
    }
}