package com.epam.esm.dto.representation;

import com.epam.esm.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class GiftCertificateRepresentationDto extends RepresentationModel<GiftCertificateRepresentationDto> {
    private Long id;
    private Set<Tag> tags;
    private String name;
    private String description;
    private Long price;
    private Integer duration;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
