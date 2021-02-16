package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateTagDto {
    @NotNull
    @Size(min = 1, max = 100, message = "name length should contain from 1 to 100 symbols")
    private String name;
}
