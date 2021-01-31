package com.epam.esm.dto.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
public class TagRepresentationDto extends RepresentationModel<TagRepresentationDto> {
    private Long id;
    private String name;
}
