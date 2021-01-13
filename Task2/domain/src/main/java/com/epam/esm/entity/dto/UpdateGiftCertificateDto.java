package com.epam.esm.entity.dto;

import com.epam.esm.entity.Tag;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateGiftCertificateDto {
    private long id;
    private Set<Tag> tags;
    private int duration;
    private long price;
    private String description;
    private String name;
}
