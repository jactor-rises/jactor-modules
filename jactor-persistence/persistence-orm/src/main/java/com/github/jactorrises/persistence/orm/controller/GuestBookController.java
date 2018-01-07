package com.github.jactorrises.persistence.orm.controller;

import com.github.jactorrises.client.dto.GuestBookDto;
import com.github.jactorrises.client.dto.GuestBookEntryDto;
import com.github.jactorrises.persistence.orm.service.GuestBookService;
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
@RequestMapping(value = "/guestBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GuestBookController extends AbstractController {

    private final GuestBookService guestBookService;

    @Autowired
    public GuestBookController(GuestBookService guestBookService) {
        this.guestBookService = guestBookService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GuestBookDto> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(guestBookService.fetch(id), HttpStatus.OK);
    }

    @PostMapping("/persist")
    public ResponseEntity<GuestBookDto> persist(@RequestBody GuestBookDto guestBookDto) {
        GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

        if (guestBookDto.getId() == null) {
            return createdWithLocation(saved, "/guestBook/get/");
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/entry/persist")
    public ResponseEntity<GuestBookEntryDto> persist(@RequestBody GuestBookEntryDto guestBookEntryDto) {
        GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

        if (guestBookEntryDto.getId() == null) {
            return createdWithLocation(saved, "/guestBook/entry/get/");
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
