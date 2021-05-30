package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.dto.BlogDto;
import com.github.jactor.persistence.dto.BlogEntryDto;
import com.github.jactor.persistence.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "/blog", produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController {

  private final BlogService blogService;

  @Autowired
  public BlogController(BlogService blogService) {
    this.blogService = blogService;
  }

  @Operation(description = "Henter en blogg ved å angi id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "En blogg for id"),
      @ApiResponse(responseCode = "204", description = "Fant ikke blog for id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<BlogDto> get(@PathVariable("id") Long blogId) {
    Optional<BlogDto> possibleBlogDto = blogService.find(blogId);

    return possibleBlogDto.map(blogDto -> new ResponseEntity<>(blogDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @Operation(description = "Henter et innslag i en blogg ved å angi id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Et blogg-innslag for id"),
      @ApiResponse(responseCode = "204", description = "Fant ikke innslaget for id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/entry/{id}")
  public ResponseEntity<BlogEntryDto> getEntryById(@PathVariable("id") Long blogEntryId) {
    Optional<BlogEntryDto> possibleEntry = blogService.findEntryBy(blogEntryId);

    return possibleEntry.map(blogEntryDto -> new ResponseEntity<>(blogEntryDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @Operation(description = "Søker etter blogger basert på en blog tittel")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Blogger basert på tittel"),
      @ApiResponse(responseCode = "204", description = "Fant ikke innslaget for id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/title/{title}")
  public ResponseEntity<List<BlogDto>> findByTitle(@PathVariable("title") String title) {
    List<BlogDto> blogsByTitle = blogService.findBlogsBy(title);

    return new ResponseEntity<>(blogsByTitle, blogsByTitle.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @Operation(description = "Søker etter blogg-innslag basert på en blogg id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Blogg-innslag basert på blogg id"),
      @ApiResponse(responseCode = "204", description = "Fant ikke innslaget for id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/{id}/entries")
  public ResponseEntity<List<BlogEntryDto>> findEntriesByBlogId(@PathVariable("id") Long blogId) {
    List<BlogEntryDto> entriesForBlog = blogService.findEntriesForBlog(blogId);

    return new ResponseEntity<>(entriesForBlog, entriesForBlog.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @Operation(description = "Endre en blogg")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Bloggen er endret"),
      @ApiResponse(responseCode = "400", description = "Kunnde ikke finne blogg til å endre", content = @Content(schema = @Schema(hidden = true)))
  })
  @PutMapping("/{blogId}")
  public ResponseEntity<BlogDto> put(@RequestBody BlogDto blogDto, @PathVariable long blogId) {
    if (blogDto.getId() == null || !blogDto.getId().equals(blogId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogDto saved = blogService.saveOrUpdate(blogDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @Operation(description = "Opprett en blogg")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Bloggen er opprettet"),
      @ApiResponse(responseCode = "400", description = "Mangler blogg å opprette", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping
  public ResponseEntity<BlogDto> post(@RequestBody BlogDto blogDto) {
    if (blogDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogDto saved = blogService.saveOrUpdate(blogDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @Operation(description = "Endrer et blogg-innslag")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Blogg-innslaget er endret"),
      @ApiResponse(responseCode = "400", description = "Mangler id til blogg-innslag som skal endres", content = @Content(schema = @Schema(hidden = true)))
  })
  @PutMapping("/entry/{blogEntryId}")
  public ResponseEntity<BlogEntryDto> putEntry(@RequestBody BlogEntryDto blogEntryDto, @PathVariable Long blogEntryId) {
    if (blogEntryDto.getId() == null || !blogEntryDto.getId().equals(blogEntryId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @Operation(description = "Oppretter et blogg-innslag")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Blogg-innslaget er opprettet"),
      @ApiResponse(responseCode = "400", description = "Mangler id til bloggen som innsaget skal legges  til", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/entry")
  public ResponseEntity<BlogEntryDto> postEntry(@RequestBody BlogEntryDto blogEntryDto) {
    if (blogEntryDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    BlogEntryDto saved = blogService.saveOrUpdate(blogEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }
}
