package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImplementation;
import com.epam.esm.validator.TagValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TagServiceTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public TagService tagService() {
            return new TagServiceImplementation();
        }

        @Bean
        public TagValidator tagValidator() {
            return new TagValidator();
        }
    }

    @Autowired
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void viewAllValidTest() {
        when(tagRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        assertEquals(0, tagService.viewAll(Pageable.unpaged()).getNumberOfElements());
    }

    @Test
    public void createTagValidTest() {
        String tagName = "tag1";
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(tagName));
        Optional<Tag> savedTag = tagService.create(tagName);
        assertTrue(savedTag.isPresent());
    }

    @Test
    public void createTagInvalidTest() {
        String tagName = "tag1$#$#$#$#";
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(tagName));
        Optional<Tag> savedTag = tagService.create(tagName);
        assertFalse(savedTag.isPresent());
    }

    @Test
    public void deleteValidTest() {
        Set<Tag> tagSet = new HashSet<>();
        Tag tag = new Tag("name");
        tag.setId(1L);
        tagSet.add(tag);
        when(tagRepository.removeAllById(any(Long.class))).thenReturn(tagSet.remove(tag) ? 1 : 0);
        assertTrue(tagSet.isEmpty());
    }

    @Test
    public void findMostUsedUserTagValidTest() {
        String userName = "1";
        Tag tag = new Tag("most popular tag");
        when(tagRepository.findMostUsedUserTag(any(Long.class))).thenReturn(new ArrayList<>(Collections.singleton(tag)));
        List<Tag> mostPopularTag = tagService.findMostUsedUserTag(userName);
        assertEquals(mostPopularTag.get(0), tag);
    }

    @Test
    public void findMostUsedUserTagInvalidTest() {
        String userName = "1asdfasfasdf";
        List<Tag> mostPopularTag = tagService.findMostUsedUserTag(userName);
        assertTrue(mostPopularTag.isEmpty());
    }
}