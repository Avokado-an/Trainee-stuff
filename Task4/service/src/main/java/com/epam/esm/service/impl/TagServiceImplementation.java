package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.dto.representation.TagRepresentationDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImplementation implements TagService {
    private TagRepository tagRepository;
    private TagValidator tagValidator;
    private ModelMapper modelMapper;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Page<TagRepresentationDto> viewAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(t -> modelMapper.map(t, TagRepresentationDto.class));
    }

    @Override
    @Transactional
    public Optional<TagRepresentationDto> create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        Optional<Tag> createdTag;
        Optional<TagRepresentationDto> tagRepresentation = Optional.empty();
        if (tagValidator.validateTag(tag)) {
            createdTag = Optional.of(tagRepository.save(tag));
            tagRepresentation = Optional.of(modelMapper.map(createdTag.get(), TagRepresentationDto.class));
        }
        return tagRepresentation;
    }

    @Override
    @Transactional
    public List<TagRepresentationDto> findMostUsedUserTag(String userId) {
        List<TagRepresentationDto> tags;
        try {
            long userIdValue = Long.parseLong(userId);
            tags = tagRepository.findMostUsedUserTag(userIdValue).stream()
                    .map(t -> modelMapper.map(t, TagRepresentationDto.class))
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    @Override
    @Transactional
    public Set<TagRepresentationDto> delete(long id) {
        tagRepository.removeAllById(id);
        return new HashSet<>(tagRepository.findAll()).stream()
                .map(t -> modelMapper.map(t, TagRepresentationDto.class))
                .collect(Collectors.toSet());
    }
}
