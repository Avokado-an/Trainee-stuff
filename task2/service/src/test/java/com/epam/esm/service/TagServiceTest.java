package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.impl.TagServiceImplementation;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagServiceTest {
    private TagRepository tagRepository;
    private TagServiceImplementation tagService;
    private TransactionManager transactionManager;

    @BeforeEach
    public void setUp() {
        tagRepository = mock(TagRepository.class);
        transactionManager = mock(TransactionManager.class);
        tagService = new TagServiceImplementation();
        tagService.setTagValidator(new TagValidator());
        tagService.setTransactionManager(transactionManager);
        tagService.setTagRepository(tagRepository);
    }

    @AfterEach
    public void deactivate() {
        tagRepository = null;
        transactionManager = null;
        tagService = null;
    }

    @Test
    public void createTagValidTest() {
        String name = "qwer";
        Tag tag = new Tag(name);
        when(tagRepository.create(any(Tag.class))).thenReturn(tag);
        Optional<Tag> actual = Optional.of(tag);
        Optional<Tag> expected = tagService.create(tag.getName());
        assertEquals(actual, expected);
    }

    public static Object[][] createTagIncorrectData() {
        return new Object[][]{
                {new Tag("qwer#<>#")},
                {new Tag("qwerffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")},
                {new Tag("")}
        };
    }

    @ParameterizedTest
    @MethodSource("createTagIncorrectData")
    public void createCertificateInvalidTest(Tag tag) {
        Optional<Tag> actual = Optional.empty();
        Optional<Tag> expected = tagService.create(tag.getName());
        assertEquals(actual, expected);
    }

    @Test
    public void readTagValidTest() {
        int actualSize = 1;
        Set<Tag> actualCertificates = new HashSet<>();
        actualCertificates.add(new Tag("111111"));
        when(tagService.viewAll()).thenReturn(actualCertificates);
        Set<Tag> expectedCertificates = tagService.viewAll();
        int expectedSize = expectedCertificates.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void deleteTagValidTest() {
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setName("qwer");
        tags.add(tag);
        doNothing().when(transactionManager).deleteTag(any(Long.class));
        when(tagRepository.read()).thenReturn(tags);
        Set<Tag> expectedTags = tagService.delete(1L);
        assertEquals(tags, expectedTags);
    }
}
