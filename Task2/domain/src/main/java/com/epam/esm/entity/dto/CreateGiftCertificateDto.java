package com.epam.esm.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CreateGiftCertificateDto {
    private Set<CreateTagDto> tags;
    private String name;
    private String description;
    private long price;
    private int duration;
}
