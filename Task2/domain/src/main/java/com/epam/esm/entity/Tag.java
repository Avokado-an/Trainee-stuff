package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class Tag {
    private long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
