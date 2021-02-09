package com.epam.esm.service.impl;

import com.epam.esm.dto.representation.TagRepresentationDto;
import com.epam.esm.entity.Tag;
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
@Transactional
public class TagServiceImplementation implements TagService {
    private TagRepository tagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<TagRepresentationDto> viewAll(Pageable pageable) {
        return tagRepository.findAll(pageable).map(t -> modelMapper.map(t, TagRepresentationDto.class));
    }

    @Override
    public TagRepresentationDto create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        Tag createdTag = tagRepository.save(tag);
        return modelMapper.map(createdTag, TagRepresentationDto.class);
    }

    @Override
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
    public Set<TagRepresentationDto> delete(long id) {
        tagRepository.removeAllById(id);
        return new HashSet<>(tagRepository.findAll()).stream()
                .map(t -> modelMapper.map(t, TagRepresentationDto.class))
                .collect(Collectors.toSet());
    }
}
