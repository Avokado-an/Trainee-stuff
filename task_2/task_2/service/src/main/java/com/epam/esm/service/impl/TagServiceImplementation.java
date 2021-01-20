package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImplementation implements TagService {
    private TagRepository tagRepository;
    private TransactionManager transactionManager;
    private TagValidator tagValidator;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public Set<Tag> viewAll() {
        return tagRepository.read();
    }

    @Override
    public Optional<Tag> create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        Optional<Tag> createdTag = Optional.empty();
        if (tagValidator.validateTag(tag)) {
            createdTag = Optional.of(tagRepository.create(tag));
        }
        return createdTag;
    }

    @Override
    public Set<Tag> delete(Long id) {
        transactionManager.deleteTag(id);
        return tagRepository.read();
    }
}
