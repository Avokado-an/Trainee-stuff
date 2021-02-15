package com.epam.esm.controller;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.IdDto;
import com.epam.esm.dto.representation.TagRepresentationDto;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.hateoas.HateoasTagManager;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;


@RestController
@RequestMapping("tags")
public class TagCertificateController {
    private TagService tagService;
    private HateoasTagManager hateoasTagManager;

    @Autowired
    public void setHateoasTagManager(HateoasTagManager hateoasTagManager) {
        this.hateoasTagManager = hateoasTagManager;
    }

    @Autowired
    public void setTagServiceImplementation(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public Page<TagRepresentationDto> showTags(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TagRepresentationDto> tags = tagService.viewAll(pageable);
        hateoasTagManager.manageTagLinks(tags, pageable);
        return tags;
    }

    @DeleteMapping
    @Secured("ROLE_ADMIN")
    public Set<TagRepresentationDto> deleteTag(@RequestBody IdDto idDto) {
        return tagService.delete(idDto.getId());
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public TagRepresentationDto createTag(@Valid @RequestBody CreateTagDto tag) {
        return tagService.create(tag.getName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandler handleResourceNotFoundException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.getErrorCode());
    }
}
