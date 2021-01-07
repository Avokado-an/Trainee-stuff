package com.epam.esm.model.dto;

import com.epam.esm.model.Tag;
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
