package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {
    Page<Tag> viewAll(Pageable pageable);

    Optional<Tag> create(String tagName);

    List<Tag> findMostUsedUserTag(String userId);

    Set<Tag> delete(long id);
}
