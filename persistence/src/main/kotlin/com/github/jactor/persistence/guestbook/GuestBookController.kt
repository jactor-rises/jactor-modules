package com.github.jactor.persistence.guestbook

import com.github.jactor.persistence.util.toCreateGuestBook
import com.github.jactor.shared.api.CreateGuestBookCommand
import com.github.jactor.shared.api.CreateGuestBookEntryCommand
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import java.util.UUID
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
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen gjestebok på id",
            )
        ]
    )
    @GetMapping("/{id}")
    @Operation(description = "Henter en gjesdebok ved å angi id")
    suspend operator fun get(@PathVariable("id") id: UUID): ResponseEntity<GuestBookDto> {
        return guestBookService.findGuestBook(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Innslaget i gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen innslag med id",
            )
        ]
    )
    @GetMapping("/entry/{id}")
    @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
    suspend fun getEntry(@PathVariable("id") id: UUID): ResponseEntity<GuestBookEntryDto> {
        return guestBookService.findEntry(id)?.let { ResponseEntity(it.toGuestBookEntryDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Gjesteboka er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller gjesteboka er allerede opprettet",
            )
        ]
    )
    @Operation(description = "Opprett en gjestebok")
    @PostMapping
    suspend fun post(
        @RequestBody createGuestBookCommand: CreateGuestBookCommand
    ): ResponseEntity<GuestBookDto> = ResponseEntity(
        guestBookService.create(createGuestBook = createGuestBookCommand.toCreateGuestBook()).toGuestBookDto(),
        HttpStatus.CREATED
    )

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller det mangler gjestebok å endre for id",
            )
        ]
    )
    @Operation(description = "Endre en gjestebok")
    @PutMapping("/update")
    suspend fun put(
        @RequestBody guestBookDto: GuestBookDto
    ): ResponseEntity<GuestBookDto> = when (guestBookDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBook(guestBookDto = guestBookDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Innslaget i gjesteboka er opprettet"),
            ApiResponse(responseCode = "400", description = "Ingen innslag som skal opprettes er gitt"),
            ApiResponse(responseCode = "400", description = "Ingen forfatter av innslag som skal opprettes er gitt"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til gjesteboka for innslaget som skal opprettes er gitt",
            ),
        ]
    )
    @Operation(description = "Opprett et innslag i en gjestebok")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody createGuestBookEntryCommand: CreateGuestBookEntryCommand
    ): ResponseEntity<GuestBookEntryDto> = ResponseEntity(
        guestBookService.create(
            createGuestBookEntry = createGuestBookEntryCommand.toCreateGuestBook()
        ).toGuestBookEntryDto(),
        HttpStatus.CREATED
    )

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Innslaget i gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag for gjestebok er gitt",
            )
        ]
    )
    @Operation(description = "Endre et innslag i en gjestebok")
    @PutMapping("/entry/update")
    suspend fun putEntry(
        @RequestBody guestBookEntryDto: GuestBookEntryDto
    ): ResponseEntity<GuestBookEntryDto> = when (guestBookEntryDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBookEntry(guestBookEntryDto = guestBookEntryDto)).toGuestBookEntryDto(),
            HttpStatus.ACCEPTED,
        )
    }
}
