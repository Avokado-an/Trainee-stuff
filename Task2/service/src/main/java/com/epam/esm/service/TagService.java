package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagService {
    Set<Tag> viewAll();

    Optional<Tag> create(String tagName);

    Set<Tag> delete(long id);
}
