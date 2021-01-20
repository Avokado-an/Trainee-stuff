package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private Set<Tag> tags;
    private Long price;
    private Integer duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
