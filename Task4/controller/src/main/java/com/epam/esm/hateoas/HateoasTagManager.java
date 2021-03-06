package com.epam.esm.hateoas;

import com.epam.esm.controller.TagCertificateController;
import com.epam.esm.dto.representation.TagRepresentationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasTagManager {
    public void manageTagLinks(Page<TagRepresentationDto> tags, Pageable pageable) {
        Link selfLink = linkTo(methodOn(TagCertificateController.class)
                .showTags(pageable)).withSelfRel();
        for (TagRepresentationDto tag : tags) {
            tag.add(selfLink);
        }
    }
}
