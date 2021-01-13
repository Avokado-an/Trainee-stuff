package com.epam.esm.repository;

import java.util.Optional;
import java.util.Set;

public interface CrdRepository<T> {
    T create(T t);

    Optional<T> read(long index);

    Set<T> read();

    boolean delete(long index);
}
