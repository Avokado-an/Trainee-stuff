package com.epam.esm.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserDto {
    private String username;
    private LocalDateTime birthday;
}
