package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.service.BlogService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/blog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BlogController {

  private final BlogService blogService;

  @Autowired
  public BlogController(BlogService blogService) {
    this.blogService = blogService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<BlogDto> get(@PathVariable("id") Long blogId) {
    Optional<BlogDto> possibleBlogDto = blogService.find(blogId);

    return possibleBlogDto.map(blogDto -> new ResponseEntity<>(blogDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @GetMapping("/entry/{id}")
  public ResponseEntity<BlogEntryDto> getEntryById(@PathVariable("id") Long blogEntryId) {
    Optional<BlogEntryDto> possibleEntry = blogService.findEntryBy(blogEntryId);

    return possibleEntry.map(blogEntryDto -> new ResponseEntity<>(blogEntryDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @GetMapping("/title/{title}")
  public ResponseEntity<List<BlogDto>> findByTitle(@PathVariable("title") String title) {
    List<BlogDto> blogsByTitle = blogService.findBlogsBy(title);

    return new ResponseEntity<>(blogsByTitle, blogsByTitle.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @GetMapping("/{id}/entries")
  public ResponseEntity<List<BlogEntryDto>> findEntriesByBlogId(@PathVariable("id") Long blogId) {
    List<BlogEntryDto> entriesForBlog = blogService.findEntriesForBlog(blogId);

    return new ResponseEntity<>(entriesForBlog, entriesForBlog.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @PutMapping("/{blogId}")
  public ResponseEntity<BlogDto> put(@RequestBody BlogDto blogDto, @PathVariable long blogId) {
    if (blogDto.getId() == null || !blogDto.getId().equals(blogId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogDto saved = blogService.saveOrUpdate(blogDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @PostMapping
  public ResponseEntity<BlogDto> post(@RequestBody BlogDto blogDto) {
    if (blogDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogDto saved = blogService.saveOrUpdate(blogDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @PutMapping("/entry/{blogEntryId}")
  public ResponseEntity<BlogEntryDto> putEntry(@RequestBody BlogEntryDto blogEntryDto, @PathVariable Long blogEntryId) {
    if (blogEntryDto.getId() == null || !blogEntryDto.getId().equals(blogEntryId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @PostMapping("/entry")
  public ResponseEntity<BlogEntryDto> postEntry(@RequestBody BlogEntryDto blogEntryDto) {
    if (blogEntryDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }
}
