package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
public class CreateGiftCertificateDto {
    @NotNull
    @Size(max = 100, message = "not more than 100 tags")
    private Set<Tag> tags;
    @NotNull
    @Size(min = 4, max = 100, message = "length should be between 4 and 100")
    private String name;

    @NotNull
    @Size(min = 10, max = 1000, message = "length should be between 10 and 1000")
    private String description;
    @NotNull
    @Min(value = 1, message = "minimal price - 1")
    @Max(value = 1000, message = "maximum price 1000")
    private Long price;
    @NotNull
    @Min(value = 1, message = "minimal duration - 1")
    @Max(value = 100, message = "max duration - 100")
    private int duration;
}
