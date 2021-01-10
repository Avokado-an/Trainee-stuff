package com.epam.esm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
public class Tag {
    private long id;
    private String name;
}
