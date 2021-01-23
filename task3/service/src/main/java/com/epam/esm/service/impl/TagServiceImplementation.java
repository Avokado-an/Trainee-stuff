package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.MostPopularTagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TagServiceImplementation implements TagService {
    private TagRepository tagRepository;
    private TagValidator tagValidator;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public Page<Tag> viewAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Optional<Tag> create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        Optional<Tag> createdTag = Optional.empty();
        if (tagValidator.validateTag(tag)) {
            createdTag = Optional.of(tagRepository.save(tag));
        }
        return createdTag;
    }

    @Override
    public List<Tag> findMostUsedUserTag(String userId) {
        List<Tag> tags;
        try {
            long userIdValue = Long.parseLong(userId);
            tags = tagRepository.findMostUsedUserTag(userIdValue);
        } catch (NumberFormatException e) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    @Override
    @Transactional
    public Set<Tag> delete(long id) {
        tagRepository.removeAllById(id);
        return new HashSet<>(tagRepository.findAll());
    }
}
