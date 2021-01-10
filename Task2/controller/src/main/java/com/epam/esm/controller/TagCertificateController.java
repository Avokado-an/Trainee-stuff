package com.epam.esm.controller;

import com.epam.esm.error.ErrorCode;
import com.epam.esm.error.ErrorHandler;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.CreateTagDto;
import com.epam.esm.model.dto.IdDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/tags")
public class TagCertificateController {
    private TagService tagService;

    @Autowired
    public void setTagServiceImplementation(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public Set<Tag> showTags() {
        return tagService.viewAll();
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
