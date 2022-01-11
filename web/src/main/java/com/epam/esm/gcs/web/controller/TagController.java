package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    private List<TagDto> findAll() {
        return tagService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> create(@RequestBody TagDto tag) {
        Long tagId = tagService.create(tag);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tagId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    private TagDto findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void update(@PathVariable Long id, @RequestBody TagDto tag) {
        tagService.update(new TagDto(id, tag.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        tagService.remove(id);
    }
}
