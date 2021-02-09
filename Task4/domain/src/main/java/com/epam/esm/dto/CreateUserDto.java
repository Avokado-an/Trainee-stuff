package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    @NotNull
    @Size(min = 3, max = 100, message = "name should contain from 3 to 100 symbols")
    private String username;
    @NotNull
    @Size(min = 6, max = 100, message = "password should contain from 6 to 100 symbols")
    private String password;
    @NotNull
    private LocalDateTime birthday;
}
