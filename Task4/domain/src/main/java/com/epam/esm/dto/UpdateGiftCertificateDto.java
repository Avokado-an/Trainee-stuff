package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateGiftCertificateDto {
    private Long id;
    private Set<Tag> tags;
    private Integer duration;
    private Long price;
    private String description;
    private String name;
}
