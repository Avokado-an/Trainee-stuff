package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.MostPopularTagDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {
    Set<Tag> viewAll();

    Optional<Tag> create(String tagName);

    List<Tag> findMostUsedUserTag(String userId);

    Set<Tag> delete(long id);
}
