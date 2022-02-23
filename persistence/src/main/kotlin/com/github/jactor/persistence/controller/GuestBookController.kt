package com.github.jactor.persistence.controller

import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.service.GuestBookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/guestBook"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GuestBookController(private val guestBookService: GuestBookService) {
    @Operation(description = "Henter en gjesdebok ved å angi id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Gjesteboka er hentet"),
            ApiResponse(responseCode = "204", description = "Fant ingen gjestebok på id", content = arrayOf(Content(schema = Schema(hidden = true))))
        ]
    )
    @GetMapping("/{id}")
    operator fun get(@PathVariable("id") id: Long): ResponseEntity<GuestBookDto> {
        return guestBookService.find(id).map { guestBookDto: GuestBookDto -> ResponseEntity(guestBookDto, HttpStatus.OK) }
            .orElseGet { ResponseEntity(HttpStatus.NO_CONTENT) }
    }

    @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Innslaget i gjesteboka er hentet"),
            ApiResponse(responseCode = "204", description = "Fant ingen innslag med id", content = arrayOf(Content(schema = Schema(hidden = true))))
        ]
    )
    @GetMapping("/entry/{id}")
    fun getEntry(@PathVariable("id") id: Long): ResponseEntity<GuestBookEntryDto> {
        return guestBookService.findEntry(id).map { guestBookDto: GuestBookEntryDto -> ResponseEntity(guestBookDto, HttpStatus.OK) }
            .orElseGet { ResponseEntity(HttpStatus.NO_CONTENT) }
    }

    @Operation(description = "Opprett en gjestebok")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Gjesteboka er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller gjesteboka er allerede opprettet",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PostMapping
    fun post(@RequestBody guestBookDto: GuestBookDto): ResponseEntity<GuestBookDto> {
        if (guestBookDto.id != null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(guestBookService.saveOrUpdate(guestBookDto), HttpStatus.CREATED)
    }

    @Operation(description = "Endre en gjestebok")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller det mangler gjestebok å endre for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PutMapping("/{guestBookId}")
    fun put(@RequestBody guestBookDto: GuestBookDto, @PathVariable guestBookId: Long): ResponseEntity<GuestBookDto> {
        if (guestBookDto.id != guestBookId) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(guestBookService.saveOrUpdate(guestBookDto), HttpStatus.ACCEPTED)
    }

    @Operation(description = "Opprett et innslag i en gjestebok")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Innslaget i gjesteboka er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag å opprette",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PostMapping("/entry")
    fun postEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.id != null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(guestBookService.saveOrUpdate(guestBookEntryDto), HttpStatus.CREATED)
    }

    @Operation(description = "Endre et innslag i en gjestebok")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Innslaget i gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag for gjestebok er gitt",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PutMapping("/entry/{guestBookEntryId}")
    fun putEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto, @PathVariable guestBookEntryId: Long): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.id != guestBookEntryId) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(guestBookService.saveOrUpdate(guestBookEntryDto), HttpStatus.ACCEPTED)
    }
}