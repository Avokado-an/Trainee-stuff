package com.epam.esm.repository;

import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Optional;
import java.util.Set;

public interface CrdRepository<T> {
    GeneratedKeyHolder create(T t);

    Optional<T> read(long index);

    Set<T> read();

    boolean delete(long index);
}
