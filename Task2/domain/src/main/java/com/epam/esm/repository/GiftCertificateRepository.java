package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import com.epam.esm.repository.type.SearchTypeQuery;
import com.epam.esm.repository.type.SortTypeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

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
    public GiftCertificate create(GiftCertificate giftCertificate) {
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
        long index = keyHolder.getKey().longValue();
        return read(index).get();
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

    public List<GiftCertificate> filterCertificates(CertificateFilterDto filter) {
        String query = createFilterQuery(filter);
        List<GiftCertificate> certificates = jdbcTemplate.query(query, new GiftCertificateMapper());
        for (GiftCertificate certificate : certificates) {
            Set<Tag> certificateTags = tagRepository.readCertificateTags(certificate.getId());
            certificate.setTags(certificateTags);
        }
        return certificates;
    }

    private String createFilterQuery(CertificateFilterDto filterDto) {
        StringBuilder filterQuery = new StringBuilder(createSearchTQuery(filterDto.getSearchTypeQueries(),
                filterDto.getTagName(), filterDto.getCertificateNameOrDescription()));
        filterQuery.append(createSortQuery(new ArrayList<>(filterDto.getSortingAlgorithm())));
        return filterQuery.toString();
    }

    private String createSearchTQuery(Set<SearchTypeQuery> searchParameters, String tagName, String nameDescription) {
        String query = SELECT_ALL;
        String replacementCharacter = "?";
        if (searchParameters.containsAll(Arrays.asList(SearchTypeQuery.values()))) {
            query = SearchTypeQuery.SEARCH_BY_TAG.getStandaloneQuery().replace(replacementCharacter, tagName);
            query += SearchTypeQuery.SEARCH_BY_NAME_DESCRIPTION.getContinuingQuery()
                    .replace(replacementCharacter, nameDescription);
        } else if (searchParameters.contains(SearchTypeQuery.SEARCH_BY_TAG)) {
            query = SearchTypeQuery.SEARCH_BY_TAG.getStandaloneQuery().replace(replacementCharacter, tagName);
        } else if (searchParameters.contains(SearchTypeQuery.SEARCH_BY_NAME_DESCRIPTION)) {
            query = SearchTypeQuery.SEARCH_BY_NAME_DESCRIPTION.getStandaloneQuery()
                    .replace(replacementCharacter, nameDescription);
        }
        return query;
    }

    private String createSortQuery(List<SortTypeQuery> sortTypeQueries) {
        StringBuilder query = new StringBuilder();
        if (!sortTypeQueries.isEmpty()) {
            query.append(sortTypeQueries.get(0).getStandaloneQuery());
        }
        for (int i = 1; i < sortTypeQueries.size(); i++) {
            query.append(sortTypeQueries.get(i).getContinuationQuery());
        }
        return query.toString();
    }
}
