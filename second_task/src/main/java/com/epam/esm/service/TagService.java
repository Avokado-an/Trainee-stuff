package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.Set;

public interface TagService {
    Set<Tag> viewAll();

    Set<Tag> create(String tagName);

    Set<Tag> delete(long id);
}
