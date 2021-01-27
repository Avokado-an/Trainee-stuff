package com.epam.esm.entity.dto;

import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CreateGiftCertificateDto {
    private Set<Tag> tags;
    private String name;
    private String description;
    private Long price;
    private int duration;
}
