package com.gitlab.jactor.rises.persistence.controller;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.persistence.service.BlogService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/blog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BlogController extends AbstractController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BlogDto> get(@PathVariable("id") Long blogId) {
        Optional<BlogDto> possibleBlogDto = blogService.find(blogId);

        return possibleBlogDto.map(blogDto -> new ResponseEntity<>(blogDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/entry/get/{id}")
    public ResponseEntity<BlogEntryDto> getEntryById(@PathVariable("id") Long blogEntryId) {
        Optional<BlogEntryDto> possibleEntry = blogService.findEntryBy(blogEntryId);

        return possibleEntry.map(blogEntryDto -> new ResponseEntity<>(blogEntryDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/find/{title}")
    public ResponseEntity<List<BlogDto>> findByTitle(@PathVariable("title") String title) {
        List<BlogDto> blogsByTitle = blogService.findBlogsBy(title);

        return new ResponseEntity<>(blogsByTitle, blogsByTitle.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/{id}/entries/find")
    public ResponseEntity<List<BlogEntryDto>> findEntriesByBlogId(@PathVariable("id") Long blogId) {
        List<BlogEntryDto> entriesForBlog = blogService.findEntriesForBlog(blogId);

        return new ResponseEntity<>(entriesForBlog, entriesForBlog.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping("/persist")
    public ResponseEntity<BlogDto> persist(@RequestBody BlogDto blogDto) {
        BlogDto saved = blogService.saveOrUpdate(blogDto);

        if (blogDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/blog/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<BlogEntryDto> persist(@RequestBody BlogEntryDto blogEntryDto) {
        BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

        if (blogEntryDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/blog/entries/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
