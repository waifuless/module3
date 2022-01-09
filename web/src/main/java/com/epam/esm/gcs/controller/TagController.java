package com.epam.esm.gcs.controller;

import com.epam.esm.gcs.dto.TagDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    @GetMapping
    private List<TagDto> findAll() {
        throw new UnsupportedOperationException();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private TagDto create(@RequestBody TagDto tag) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{id}")
    private TagDto findById(@PathVariable String id) {
        throw new UnsupportedOperationException();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private TagDto createOrUpdate(@PathVariable String id, @RequestBody TagDto tag) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{id}")
    private TagDto delete(@PathVariable String id) {
        throw new UnsupportedOperationException();
    }
}
