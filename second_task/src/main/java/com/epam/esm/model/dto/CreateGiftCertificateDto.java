package com.epam.esm.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CreateGiftCertificateDto {
    private Set<CreateTagDto> tags;
    private String name;
    private String description;
    private long price;
    private int duration;
}
