package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.dto.CreateTagDto;
import com.epam.esm.entity.dto.IdDto;
import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static com.epam.esm.hateoas.HateoasTagManager.manageTagLinks;

@RestController
@RequestMapping("tags")
public class TagCertificateController {
    private TagService tagService;

    @Autowired
    public void setTagServiceImplementation(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public Page<Tag> showTags(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Tag> tags = tagService.viewAll(pageable);
        manageTagLinks(tags, pageable);
        return tags;
    }

    @DeleteMapping
    public Set<Tag> deleteTag(@RequestBody IdDto idDto) {
        return tagService.delete(idDto.getId());
    }

    @PostMapping
    public Optional<Tag> createTag(@RequestBody CreateTagDto tag) {
        return tagService.create(tag.getName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorHandler handleIncorrectParameterValueException(Exception exception) {
        return new ErrorHandler(exception.getMessage(), ErrorCode.RESOURCE_NOT_FOUND);
    }
}
