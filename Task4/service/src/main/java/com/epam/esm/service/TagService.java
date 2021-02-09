package com.epam.esm.service;

import com.epam.esm.dto.representation.TagRepresentationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TagService {
    Page<TagRepresentationDto> viewAll(Pageable pageable);

    TagRepresentationDto create(String tagName);

    List<TagRepresentationDto> findMostUsedUserTag(String userId);

    Set<TagRepresentationDto> delete(long id);
}
