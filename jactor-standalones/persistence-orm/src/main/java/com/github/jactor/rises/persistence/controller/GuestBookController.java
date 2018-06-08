package com.github.jactor.rises.persistence.controller;

import com.github.jactor.rises.client.dto.NewGuestBookDto;
import com.github.jactor.rises.client.dto.NewGuestBookEntryDto;
import com.github.jactor.rises.persistence.service.GuestBookService;
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
    public ResponseEntity<NewGuestBookDto> get(@PathVariable("id") Long id) {
        Optional<NewGuestBookDto> foundGuestBookDto = guestBookService.find(id);

        return foundGuestBookDto
                .map(newGuestBookDto -> new ResponseEntity<>(newGuestBookDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

    }

    @GetMapping("/get/entry/{id}")
    public ResponseEntity<NewGuestBookEntryDto> getEntry(@PathVariable("id") Long id) {
        Optional<NewGuestBookEntryDto> foundGuestBookEntryDto = guestBookService.findEntry(id);

        return foundGuestBookEntryDto
                .map(newGuestBookDto -> new ResponseEntity<>(newGuestBookDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping("/persist")
    public ResponseEntity<NewGuestBookDto> persist(@RequestBody NewGuestBookDto guestBookDto) {
        NewGuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

        if (guestBookDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/guestBook/get/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<NewGuestBookEntryDto> persist(@RequestBody NewGuestBookEntryDto guestBookEntryDto) {
        NewGuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

        if (guestBookEntryDto.getId() == null) {
            return aCreatedResponseEntity(saved, String.format("/guestBook/get/entry/%s", saved.getId()));
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
