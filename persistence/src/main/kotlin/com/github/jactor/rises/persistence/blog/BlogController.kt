package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.util.toBlog
import com.github.jactor.rises.persistence.util.toBlogEntry
import com.github.jactor.rises.persistence.util.toCreateBlogEntry
import com.github.jactor.rises.persistence.util.toUpdateBlogTitle
import com.github.jactor.rises.shared.api.BlogDto
import com.github.jactor.rises.shared.api.BlogEntryDto
import com.github.jactor.rises.shared.api.CreateBlogEntryCommand
import com.github.jactor.rises.shared.api.UpdateBlogTitleCommand
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
@RequestMapping(value = ["/blog"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val blogService: BlogService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "En blogg for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke blog for id",
            )
        ]
    )

    @Operation(description = "Henter en blogg ved å angi id")
    @GetMapping("/{id}")
    suspend operator fun get(@PathVariable("id") blogId: UUID): ResponseEntity<BlogDto> {
        return blogService.find(blogId)?.let { ResponseEntity(it.toBlogDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Et blogg-innslag for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
            )
        ]
    )

    @Operation(description = "Henter et innslag i en blogg ved å angi id")
    @GetMapping("/entry/{id}")
    suspend fun getEntryById(@PathVariable("id") blogEntryId: UUID): ResponseEntity<BlogEntryDto> {
        return blogService.findEntryBy(blogEntryId)?.let { ResponseEntity(it.toBlogEntryDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogger basert på tittel"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
            )
        ]
    )

    @GetMapping("/title/{title}")
    @Operation(description = "Søker etter blogger basert på en blog tittel")
    suspend fun findByTitle(@PathVariable("title") title: String): ResponseEntity<List<BlogDto>> {
        val blogsByTitle = blogService.findBlogsBy(title)
            .map { it.toBlogDto() }

        return when (blogsByTitle.isNotEmpty()) {
            true -> ResponseEntity(blogsByTitle, HttpStatus.OK)
            false -> ResponseEntity(HttpStatus.NO_CONTENT)
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogg-innslag basert på blogg id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
            )
        ]
    )

    @GetMapping("/{id}/entries")
    @Operation(description = "Søker etter blogg-innslag basert på en blogg id")
    suspend fun findEntriesByBlogId(@PathVariable("id") blogId: UUID): ResponseEntity<List<BlogEntryDto>> {
        val entriesForBlog = blogService.findEntriesForBlog(blogId)
            .map { it.toBlogEntryDto() }

        return when (entriesForBlog.isNotEmpty()) {
            true -> ResponseEntity(entriesForBlog, HttpStatus.OK)
            false -> ResponseEntity(HttpStatus.NO_CONTENT)
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Bloggen er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Kunnde ikke finne blogg til å endre",
            )
        ]
    )

    @Operation(description = "Endre en blogg")
    @PutMapping("/{blogId}")
    suspend fun put(
        @RequestBody updateBlogTitleCommand: UpdateBlogTitleCommand, @PathVariable blogId: UUID
    ): ResponseEntity<BlogDto> = (updateBlogTitleCommand.blogId ?: blogId).let {
        ResponseEntity(
            blogService.update(updateBlogTitle = updateBlogTitleCommand.toUpdateBlogTitle()).toBlogDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Bloggen er opprettet"),
            ApiResponse(responseCode = "400", description = "Mangler blogg å opprette"),
            ApiResponse(responseCode = "400", description = "Har allerede id på blogg som opprettes"),
        ]
    )
    @Operation(description = "Opprett en blogg")
    @PostMapping
    suspend fun post(@RequestBody blogDto: BlogDto): ResponseEntity<BlogDto> = when (blogDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blog = blogDto.toBlog()).toBlogDto(),
            HttpStatus.CREATED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Blogg-innslaget er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til blogg-innslag som skal endres",
            )
        ]
    )
    @Operation(description = "Endrer et blogg-innslag")
    @PutMapping("/entry/{blogEntryId}")
    suspend fun putEntry(
        @RequestBody blogEntryDto: BlogEntryDto,
        @PathVariable blogEntryId: UUID
    ): ResponseEntity<BlogEntryDto> = when (blogEntryDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blogEntry = blogEntryDto.toBlogEntry()).toBlogEntryDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Blogg-innslaget er opprettet"),
            ApiResponse(responseCode = "400", description = "Mangler id til bloggen som innslaget skal legges til"),
            ApiResponse(responseCode = "400", description = "Mangler navn til forfatter av innslag"),
            ApiResponse(responseCode = "400", description = "Mangler innslaget som skal legges inn"),
        ]
    )
    @Operation(description = "Oppretter et blogg-innslag")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody createBlogEntryCommand: CreateBlogEntryCommand
    ): ResponseEntity<BlogEntryDto> = createBlogEntryCommand.toCreateBlogEntry().let {
        blogService.create(createBlogEntry = it)
    }.toBlogEntryDto().let {
        ResponseEntity(it, HttpStatus.CREATED)
    }
}
