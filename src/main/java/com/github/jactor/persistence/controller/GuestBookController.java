package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.service.GuestBookService;
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
@RequestMapping(value = "/guestBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GuestBookController {

  private final GuestBookService guestBookService;

  @Autowired
  public GuestBookController(GuestBookService guestBookService) {
    this.guestBookService = guestBookService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<GuestBookDto> get(@PathVariable("id") Long id) {
    Optional<GuestBookDto> foundGuestBookDto = guestBookService.find(id);

    return foundGuestBookDto
        .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

  }

  @GetMapping("/entry/{id}")
  public ResponseEntity<GuestBookEntryDto> getEntry(@PathVariable("id") Long id) {
    Optional<GuestBookEntryDto> foundGuestBookEntryDto = guestBookService.findEntry(id);

    return foundGuestBookEntryDto
        .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @PostMapping
  public ResponseEntity<GuestBookDto> post(@RequestBody GuestBookDto guestBookDto) {
    if (guestBookDto == null || guestBookDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @PutMapping("/{guestBookId}")
  public ResponseEntity<GuestBookDto> put(@RequestBody GuestBookDto guestBookDto, @PathVariable Long guestBookId) {
    if (guestBookDto == null || guestBookDto.getId() == null || !guestBookDto.getId().equals(guestBookId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @PostMapping("/entry")
  public ResponseEntity<GuestBookEntryDto> postEntry(@RequestBody GuestBookEntryDto guestBookEntryDto) {
    if (guestBookEntryDto == null || guestBookEntryDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @PutMapping("/entry/{guestBookEntryId}")
  public ResponseEntity<GuestBookEntryDto> putEntry(@RequestBody GuestBookEntryDto guestBookEntryDto, @PathVariable Long guestBookEntryId) {
    if (guestBookEntryDto == null || guestBookEntryDto.getId() == null || !guestBookEntryDto.getId().equals(guestBookEntryId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }
}
