package com.epam.esm.service.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.TransactionManager;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TagServiceImplementation implements TagService {
    private TagRepository tagRepository;
    private TransactionManager transactionManager;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Set<Tag> viewAll() {
        return tagRepository.read();
    }

    @Override
    public Set<Tag> create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagRepository.create(tag);
        return tagRepository.read();
    }

    @Override
    public Set<Tag> delete(long id) {
        transactionManager.deleteTag(id);
        return tagRepository.read();
    }
}
