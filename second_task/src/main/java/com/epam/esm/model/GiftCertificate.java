package com.epam.esm.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data//todo should I do eq hash by id or bad idea???
public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private Set<Tag> tags;
    private long price;
    private int duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
