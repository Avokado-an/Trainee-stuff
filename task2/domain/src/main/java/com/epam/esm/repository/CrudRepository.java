package com.epam.esm.repository;

import java.util.Optional;

public interface CrudRepository<T> extends CrdRepository<T> {
    Optional<T> update(T newInstance);
}
