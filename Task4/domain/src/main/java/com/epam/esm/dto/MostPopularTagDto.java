package com.epam.esm.dto;

import lombok.Data;

@Data
public class MostPopularTagDto {
    private String name;
    private Integer usageCount;
}
