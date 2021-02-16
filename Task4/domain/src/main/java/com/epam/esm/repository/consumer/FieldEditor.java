package com.epam.esm.repository.consumer;

@FunctionalInterface
public interface FieldEditor<T, R> {
    void edit(T t, R r);
}
