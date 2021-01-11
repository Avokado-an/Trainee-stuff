package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.dto.CertificateFilterDto;
import com.epam.esm.repository.type.SearchTypeQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateRepositoryTest {
    private GiftCertificateRepository giftCertificateRepository;
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:query/create_certificate_table.sql")
                .addScript("classpath:query/fill_certificate_table.sql")
                .addScript("classpath:query/create_certificate_tag_table.sql")
                .addScript("classpath:query/fill_certificate_tag_table.sql")
                .addScript("classpath:query/create_tag_table.sql")
                .addScript("classpath:query/fill_tag_table.sql")
                .build();
        giftCertificateRepository = new GiftCertificateRepository(dataSource, new TagRepository(dataSource));
    }

    @AfterEach
    public void deactivate() {
        new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:query/delete_certificate_table.sql")
                .addScript("classpath:query/delete_tag_table.sql")
                .addScript("classpath:query/delete_certificate_tag_table.sql")
                .build();
        giftCertificateRepository = null;
    }

    @Test
    public void addGiftCertificateValidTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(5)
                .duration(5)
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(new HashSet<>())
                .build();
        GiftCertificate created = giftCertificateRepository.create(giftCertificate);
        assertNotNull(created);
    }

    @Test
    public void addGiftCertificateInvalidTest() {
        StringBuilder name = new StringBuilder("namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename" +
                "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename");
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name(name.toString())
                .description("description")
                .price(5)
                .duration(5)
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(new HashSet<>())
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> giftCertificateRepository.create(giftCertificate));
    }

    @Test
    public void deleteCertificateValidTest() {
        int id = 1;
        boolean isDeleted = giftCertificateRepository.delete(id);
        assertTrue(isDeleted);
    }

    @Test
    public void deleteCertificateInvalidTest() {
        int id = 1000;
        boolean isDeleted = giftCertificateRepository.delete(id);
        assertFalse(isDeleted);
    }

    @Test
    public void readAllValidTest() {
        int expectedSize = 5;
        Set<GiftCertificate> certificateList = giftCertificateRepository.read();
        assertEquals(expectedSize, certificateList.size());
    }

    @Test
    public void readByIdValidTest() {
        int id = 1;
        Optional<GiftCertificate> certificate = giftCertificateRepository.read(id);
        assertTrue(certificate.isPresent());
    }

    @Test
    public void readBuIdInvalidTest() {
        int id = 5000;
        Optional<GiftCertificate> certificate = giftCertificateRepository.read(id);
        assertFalse(certificate.isPresent());
    }

    @Test
    public void updateCertificateValidTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(5)
                .duration(5)
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(new HashSet<>())
                .id(2)
                .build();
        Optional<GiftCertificate> updatedCertificate = giftCertificateRepository.update(giftCertificate);
        assertTrue(updatedCertificate.isPresent());
    }

    @Test
    public void updateCertificateInvalidTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(5)
                .duration(5)
                .creationDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(new HashSet<>())
                .id(2000)
                .build();
        Optional<GiftCertificate> updatedCertificate = giftCertificateRepository.update(giftCertificate);
        assertFalse(updatedCertificate.isPresent());
    }

    @Test
    public void filterCertificatesByNameValidTest() {
        String description = "desc1";
        Set<SearchTypeQuery> searchTypeQueries = new HashSet<>();
        searchTypeQueries.add(SearchTypeQuery.SEARCH_BY_NAME_DESCRIPTION);
        CertificateFilterDto filter = CertificateFilterDto
                .builder()
                .certificateNameOrDescription(description)
                .searchTypeQueries(searchTypeQueries)
                .sortingAlgorithm(new HashSet<>())
                .build();
        List<GiftCertificate> filteredCertificates = giftCertificateRepository.filterCertificates(filter);
        int expectedSize = 1;
        assertEquals(expectedSize, filteredCertificates.size());
    }

    @Test
    public void filterCertificatesByTagValidTest() {
        String tagName = "tag2";
        Set<SearchTypeQuery> searchTypeQueries = new HashSet<>();
        searchTypeQueries.add(SearchTypeQuery.SEARCH_BY_TAG);
        CertificateFilterDto filter = CertificateFilterDto
                .builder()
                .tagName(tagName)
                .searchTypeQueries(searchTypeQueries)
                .sortingAlgorithm(new HashSet<>())
                .build();
        List<GiftCertificate> filteredCertificates = giftCertificateRepository.filterCertificates(filter);
        int expectedSize = 2;
        assertEquals(expectedSize, filteredCertificates.size());
    }

    @Test
    public void filterCertificatesByTagAndNameValidTest() {
        String tagName = "tag2";
        String certificateName = "cer1";
        Set<SearchTypeQuery> searchTypeQueries = new HashSet<>();
        searchTypeQueries.add(SearchTypeQuery.SEARCH_BY_TAG);
        searchTypeQueries.add(SearchTypeQuery.SEARCH_BY_NAME_DESCRIPTION);
        CertificateFilterDto filter = CertificateFilterDto
                .builder()
                .tagName(tagName)
                .searchTypeQueries(searchTypeQueries)
                .sortingAlgorithm(new HashSet<>())
                .certificateNameOrDescription(certificateName)
                .build();
        List<GiftCertificate> filteredCertificates = giftCertificateRepository.filterCertificates(filter);
        int expectedSize = 1;
        assertEquals(expectedSize, filteredCertificates.size());
    }
}
