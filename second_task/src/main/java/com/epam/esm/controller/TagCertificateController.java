package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.CreateTagDto;
import com.epam.esm.model.dto.IdDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Set<Tag> createTag(@RequestBody CreateTagDto tag) {
        return tagService.create(tag.getName());
    }
}
