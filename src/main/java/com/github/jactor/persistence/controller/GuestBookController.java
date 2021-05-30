package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.dto.GuestBookDto;
import com.github.jactor.persistence.dto.GuestBookEntryDto;
import com.github.jactor.persistence.service.GuestBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "/guestBook", produces = MediaType.APPLICATION_JSON_VALUE)
public class GuestBookController {

  private final GuestBookService guestBookService;

  @Autowired
  public GuestBookController(GuestBookService guestBookService) {
    this.guestBookService = guestBookService;
  }

  @Operation(description = "Henter en gjesdebok ved å angi id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Gjesteboka er hentet"),
      @ApiResponse(responseCode = "204", description = "Fant ingen gjestebok på id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<GuestBookDto> get(@PathVariable("id") Long id) {
    Optional<GuestBookDto> foundGuestBookDto = guestBookService.find(id);

    return foundGuestBookDto
        .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

  }

  @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Innslaget i gjesteboka er hentet"),
      @ApiResponse(responseCode = "204", description = "Fant ingen innslag med id", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/entry/{id}")
  public ResponseEntity<GuestBookEntryDto> getEntry(@PathVariable("id") Long id) {
    Optional<GuestBookEntryDto> foundGuestBookEntryDto = guestBookService.findEntry(id);

    return foundGuestBookEntryDto
        .map(guestBookDto -> new ResponseEntity<>(guestBookDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @Operation(description = "Opprett en gjestebok")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Gjesteboka er opprettet"),
      @ApiResponse(responseCode = "400", description = "Ingen gjestebok er gitt eller gjesteboka er allerede opprettet", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping
  public ResponseEntity<GuestBookDto> post(@RequestBody GuestBookDto guestBookDto) {
    if (guestBookDto == null || guestBookDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @Operation(description = "Endre en gjestebok")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Gjesteboka er endret"),
      @ApiResponse(responseCode = "400", description = "Ingen gjestebok er gitt eller det mangler gjestebok å endre for id", content = @Content(schema = @Schema(hidden = true)))
  })
  @PutMapping("/{guestBookId}")
  public ResponseEntity<GuestBookDto> put(@RequestBody GuestBookDto guestBookDto, @PathVariable Long guestBookId) {
    if (guestBookDto == null || guestBookDto.getId() == null || !guestBookDto.getId().equals(guestBookId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookDto saved = guestBookService.saveOrUpdate(guestBookDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @Operation(description = "Opprett et innslag i en gjestebok")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Innslaget i gjesteboka er opprettet"),
      @ApiResponse(responseCode = "400", description = "Ingen innslag eller id til innslag å opprette", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/entry")
  public ResponseEntity<GuestBookEntryDto> postEntry(@RequestBody GuestBookEntryDto guestBookEntryDto) {
    if (guestBookEntryDto == null || guestBookEntryDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @Operation(description = "Endre et innslag i en gjestebok")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Innslaget i gjesteboka er endret"),
      @ApiResponse(responseCode = "400", description = "Ingen innslag eller id til innslag for gjestebok er gitt", content = @Content(schema = @Schema(hidden = true)))
  })
  @PutMapping("/entry/{guestBookEntryId}")
  public ResponseEntity<GuestBookEntryDto> putEntry(@RequestBody GuestBookEntryDto guestBookEntryDto, @PathVariable Long guestBookEntryId) {
    if (guestBookEntryDto == null || guestBookEntryDto.getId() == null || !guestBookEntryDto.getId().equals(guestBookEntryId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    GuestBookEntryDto saved = guestBookService.saveOrUpdate(guestBookEntryDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }
}
