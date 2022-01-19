package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
//@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    @GetMapping
    private List<TagDto> findAll() {
        return tagService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private TagDto create(@Valid @RequestBody TagDto tag) {
        return tagService.create(tag);
    }

//    @GetMapping("/{id}")
//    private TagDto findById(@PathVariable @Positive Long id) {
//        return tagService.findById(id);
//    }

    @GetMapping("/{id}")
    private TagDto findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    private void delete(@PathVariable @Positive Long id) {
//        tagService.delete(id);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        tagService.delete(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return "MethodArgumentNotValidException: " + e.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        return "ConstraintViolationException: " + e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOtherExceptions(Exception e) {
        return e.getClass() + " exception: " + e.getMessage();
    }
}
