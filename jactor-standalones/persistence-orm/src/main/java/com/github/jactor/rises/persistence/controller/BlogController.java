package com.github.jactor.rises.persistence.controller;

import com.github.jactor.rises.client.dto.NewBlogDto;
import com.github.jactor.rises.client.dto.NewBlogEntryDto;
import com.github.jactor.rises.persistence.service.BlogService;
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
    public ResponseEntity<NewBlogDto> get(@PathVariable("id") Long blogId) {
        Optional<NewBlogDto> possibleBlogDto = blogService.find(blogId);

        return possibleBlogDto.map(newBlogDto -> new ResponseEntity<>(newBlogDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/entry/get/{id}")
    public ResponseEntity<NewBlogEntryDto> getEntryById(@PathVariable("id") Long blogEntryId) {
        Optional<NewBlogEntryDto> possibleEntry = blogService.findEntryBy(blogEntryId);

        return possibleEntry.map(newBlogEntryDto -> new ResponseEntity<>(newBlogEntryDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/find/{title}")
    public ResponseEntity<List<NewBlogDto>> findByTitle(@PathVariable("title") String title) {
        List<NewBlogDto> blogsByTitle = blogService.findBlogsBy(title);

        return new ResponseEntity<>(blogsByTitle, blogsByTitle.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/{id}/entries/find")
    public ResponseEntity<List<NewBlogEntryDto>> findEntriesByBlogId(@PathVariable("id") Long blogId) {
        List<NewBlogEntryDto> entriesForBlog = blogService.findEntriesForBlog(blogId);

        return new ResponseEntity<>(entriesForBlog, entriesForBlog.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping("/persist")
    public ResponseEntity<NewBlogDto> persist(@RequestBody NewBlogDto blogDto) {
        NewBlogDto saved = blogService.saveOrUpdate(blogDto);

        if (blogDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/blog/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<NewBlogEntryDto> persist(@RequestBody NewBlogEntryDto blogEntryDto) {
        NewBlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

        if (blogEntryDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/blog/entries/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
