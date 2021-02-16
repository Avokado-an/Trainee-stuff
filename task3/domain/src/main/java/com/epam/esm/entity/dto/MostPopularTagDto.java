package com.epam.esm.entity.dto;

import lombok.Data;

@Data
public class MostPopularTagDto {
    private String name;
    private Integer usageCount;
}
