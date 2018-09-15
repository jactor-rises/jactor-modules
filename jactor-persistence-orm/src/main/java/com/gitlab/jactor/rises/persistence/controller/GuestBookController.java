package com.gitlab.jactor.rises.persistence.controller;

import com.gitlab.jactor.rises.commons.dto.GuestBookDto;
import com.gitlab.jactor.rises.commons.dto.GuestBookEntryDto;
import com.gitlab.jactor.rises.persistence.service.GuestBookService;
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

import java.util.Optional;

@RestController
@RequestMapping(value = "/guestBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GuestBookController extends AbstractController {

    private final GuestBookService guestBookService;

    @Autowired
    public GuestBookController(GuestBookService guestBookService) {
        this.guestBookService = guestBookService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GuestBookDto> get(@PathVariable("id") Long id) {
        Optional<GuestBookDto> foundGuestBookDto = guestBookService.find(id);

        return foundGuestBookDto
                .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

    }

    @GetMapping("/entry/get/{id}")
    public ResponseEntity<GuestBookEntryDto> getEntry(@PathVariable("id") Long id) {
        Optional<GuestBookEntryDto> foundGuestBookEntryDto = guestBookService.findEntry(id);

        return foundGuestBookEntryDto
                .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping("/persist")
    public ResponseEntity<GuestBookDto> persist(@RequestBody GuestBookDto guestBookDto) {
        GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

        if (guestBookDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/guestBook/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<GuestBookEntryDto> persist(@RequestBody GuestBookEntryDto guestBookEntryDto) {
        GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

        if (guestBookEntryDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/guestBook/get/entry/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
