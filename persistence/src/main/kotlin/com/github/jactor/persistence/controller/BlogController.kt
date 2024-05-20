package com.github.jactor.persistence.controller

import java.util.UUID
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.service.BlogService
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
@RequestMapping(value = ["/blog"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val blogService: BlogService) {

    @Operation(description = "Henter en blogg ved å angi id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "En blogg for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke blog for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/{id}")
    operator fun get(@PathVariable("id") blogId: UUID): ResponseEntity<BlogDto> {
        return blogService.find(blogId)?.let { ResponseEntity(it, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Operation(description = "Henter et innslag i en blogg ved å angi id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Et blogg-innslag for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/entry/{id}")
    fun getEntryById(@PathVariable("id") blogEntryId: UUID): ResponseEntity<BlogEntryDto> {
        return blogService.findEntryBy(blogEntryId)?.let { ResponseEntity(it, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Operation(description = "Søker etter blogger basert på en blog tittel")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogger basert på tittel"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/title/{title}")
    fun findByTitle(@PathVariable("title") title: String?): ResponseEntity<List<BlogDto>> {
        val blogsByTitle = blogService.findBlogsBy(title)

        return ResponseEntity(blogsByTitle, if (blogsByTitle.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK)
    }

    @Operation(description = "Søker etter blogg-innslag basert på en blogg id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogg-innslag basert på blogg id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/{id}/entries")
    fun findEntriesByBlogId(@PathVariable("id") blogId: UUID): ResponseEntity<List<BlogEntryDto>> {
        val entriesForBlog = blogService.findEntriesForBlog(blogId)

        return ResponseEntity(entriesForBlog, if (entriesForBlog.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK)
    }

    @Operation(description = "Endre en blogg")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Bloggen er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Kunnde ikke finne blogg til å endre",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PutMapping("/{blogId}")
    fun put(@RequestBody blogDto: BlogDto, @PathVariable blogId: UUID): ResponseEntity<BlogDto> {
        if (blogDto.id != blogId) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(blogService.saveOrUpdate(blogDto), HttpStatus.ACCEPTED)
    }

    @Operation(description = "Opprett en blogg")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Bloggen er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler blogg å opprette",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PostMapping
    fun post(@RequestBody blogDto: BlogDto): ResponseEntity<BlogDto> {
        if (blogDto.id != null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(blogService.saveOrUpdate(blogDto), HttpStatus.CREATED)
    }

    @Operation(description = "Endrer et blogg-innslag")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Blogg-innslaget er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til blogg-innslag som skal endres",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PutMapping("/entry/{blogEntryId}")
    fun putEntry(
        @RequestBody blogEntryDto: BlogEntryDto,
        @PathVariable blogEntryId: UUID
    ): ResponseEntity<BlogEntryDto> {
        if (blogEntryDto.id != blogEntryId) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(blogService.saveOrUpdate(blogEntryDto), HttpStatus.ACCEPTED)
    }

    @Operation(description = "Oppretter et blogg-innslag")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Blogg-innslaget er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til bloggen som innsaget skal legges  til",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @PostMapping("/entry")
    fun postEntry(@RequestBody blogEntryDto: BlogEntryDto): ResponseEntity<BlogEntryDto> {
        if (blogEntryDto.id != null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(blogService.saveOrUpdate(blogEntryDto), HttpStatus.CREATED)
    }
}