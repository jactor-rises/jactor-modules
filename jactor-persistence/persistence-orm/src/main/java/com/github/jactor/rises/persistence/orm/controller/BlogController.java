package com.github.jactor.rises.persistence.orm.controller;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.persistence.orm.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/blog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BlogController extends AbstractController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BlogDto> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(blogService.fetch(id), HttpStatus.OK);
    }

    @PostMapping("/persist")
    public ResponseEntity<BlogDto> persist(@RequestBody BlogDto blogDto) {
        BlogDto saved = blogService.saveOrUpdate(blogDto);

        if (blogDto.getId() == null) {
            return createdWithLocation(saved, "/blog/get/");
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<BlogEntryDto> persist(@RequestBody BlogEntryDto blogEntryDto) {
        BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

        if (blogEntryDto.getId() == null) {
            return createdWithLocation(saved, "/blog/entry/get/");
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
