package com.github.jactor.persistence.api.controller

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.github.jactor.persistence.blog.BlogEntryModel
import com.github.jactor.persistence.blog.BlogModel
import com.github.jactor.persistence.blog.BlogService
import com.github.jactor.persistence.guestbook.GuestBookEntryModel
import com.github.jactor.persistence.guestbook.GuestBookModel
import com.github.jactor.persistence.guestbook.GuestBookService
import com.github.jactor.persistence.harIdentifikator
import com.github.jactor.persistence.harIkkeIdentifikator
import com.github.jactor.persistence.user.UserEntity
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.user.UserService
import com.github.jactor.shared.whenTrue
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.github.jactor.shared.api.UserDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping(value = ["/blog"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val blogService: BlogService) {
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
    @Operation(description = "Henter en blogg ved å angi id")
    @GetMapping("/{id}")
    operator fun get(@PathVariable("id") blogId: UUID): ResponseEntity<BlogDto> {
        return blogService.find(blogId)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

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
    @Operation(description = "Henter et innslag i en blogg ved å angi id")
    @GetMapping("/entry/{id}")
    fun getEntryById(@PathVariable("id") blogEntryId: UUID): ResponseEntity<BlogEntryDto> {
        return blogService.findEntryBy(blogEntryId)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

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
    @Operation(description = "Søker etter blogger basert på en blog tittel")
    fun findByTitle(@PathVariable("title") title: String?): ResponseEntity<List<BlogDto>> {
        val blogsByTitle = blogService.findBlogsBy(title)
            .map { it.toDto() }

        val httpStatus = blogsByTitle.isNotEmpty().whenTrue { HttpStatus.OK } ?: HttpStatus.NO_CONTENT
        return ResponseEntity(blogsByTitle, httpStatus)
    }

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
    @Operation(description = "Søker etter blogg-innslag basert på en blogg id")
    fun findEntriesByBlogId(@PathVariable("id") blogId: UUID): ResponseEntity<List<BlogEntryDto>> {
        val entriesForBlog = blogService.findEntriesForBlog(blogId)
            .map { it.toDto() }

        return ResponseEntity(entriesForBlog, if (entriesForBlog.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK)
    }

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
    @Operation(description = "Endre en blogg")
    @PutMapping("/{blogId}")
    fun put(@RequestBody blogDto: BlogDto, @PathVariable blogId: UUID): ResponseEntity<BlogDto> {
        if (blogDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(
            blogService.saveOrUpdate(blogModel = BlogModel(blogDto = blogDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

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
    @Operation(description = "Opprett en blogg")
    @PostMapping
    fun post(@RequestBody blogDto: BlogDto): ResponseEntity<BlogDto> {
        if (blogDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(blogService.saveOrUpdate(blogModel = BlogModel(blogDto)).toDto(), HttpStatus.CREATED)
    }

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
    @Operation(description = "Endrer et blogg-innslag")
    @PutMapping("/entry/{blogEntryId}")
    fun putEntry(
        @RequestBody blogEntryDto: BlogEntryDto,
        @PathVariable blogEntryId: UUID
    ): ResponseEntity<BlogEntryDto> {
        if (blogEntryDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(
            blogService.saveOrUpdate(blogEntryModel = BlogEntryModel(blogEntry = blogEntryDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

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
    @Operation(description = "Oppretter et blogg-innslag")
    @PostMapping("/entry")
    fun postEntry(@RequestBody blogEntryDto: BlogEntryDto): ResponseEntity<BlogEntryDto> {
        if (blogEntryDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val createdBlogEntryModel = blogService.saveOrUpdate(
            blogEntryModel = BlogEntryModel(blogEntry = blogEntryDto)
        )

        val blogEntryResponseDto = createdBlogEntryModel.toDto()
        return ResponseEntity(blogEntryResponseDto, HttpStatus.CREATED)
    }
}

@RestController
@RequestMapping(value = ["/guestBook"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GuestBookController(private val guestBookService: GuestBookService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen gjestebok på id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/{id}")
    @Operation(description = "Henter en gjesdebok ved å angi id")
    operator fun get(@PathVariable("id") id: UUID): ResponseEntity<GuestBookDto> {
        return guestBookService.find(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Innslaget i gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen innslag med id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/entry/{id}")
    @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
    fun getEntry(@PathVariable("id") id: UUID): ResponseEntity<GuestBookEntryDto> {
        return guestBookService.findEntry(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

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
    @Operation(description = "Opprett en gjestebok")
    @PostMapping
    fun post(@RequestBody guestBookDto: GuestBookDto): ResponseEntity<GuestBookDto> {
        if (guestBookDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val guestBookModel = guestBookService.saveOrUpdate(GuestBookModel(guestBookDto = guestBookDto))
        return ResponseEntity(guestBookModel.toDto(), HttpStatus.CREATED)
    }

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
    @Operation(description = "Endre en gjestebok")
    @PutMapping("/update")
    fun put(@RequestBody guestBookDto: GuestBookDto): ResponseEntity<GuestBookDto> {
        if (guestBookDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val guestBookModel = GuestBookModel(guestBookDto = guestBookDto)
        return ResponseEntity(guestBookService.saveOrUpdate(guestBookModel).toDto(), HttpStatus.ACCEPTED)
    }

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
    @Operation(description = "Opprett et innslag i en gjestebok")
    @PostMapping("/entry")
    fun postEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val dto = guestBookService.saveOrUpdate(GuestBookEntryModel(guestBookEntryDto)).toDto()
        return ResponseEntity(dto, HttpStatus.CREATED)
    }

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
    @Operation(description = "Endre et innslag i en gjestebok")
    @PutMapping("/entry/update")
    fun putEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(
            guestBookService.saveOrUpdate(GuestBookEntryModel(guestBookEntryDto = guestBookEntryDto)).toDto(),
            HttpStatus.ACCEPTED,
        )
    }
}

@RestController
@RequestMapping(path = ["/user"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userService: UserService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "204", description = "No user with username")
        ]
    )
    @GetMapping("/name/{username}")
    @Operation(description = "Find a user by its username")
    fun find(@PathVariable("username") username: String): ResponseEntity<UserDto> {
        return userService.find(username = username)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User got"),
            ApiResponse(responseCode = "404", description = "Did not find user with id")
        ]
    )
    @GetMapping("/{id}")
    @Operation(description = "Get a user by its id")
    operator fun get(@PathVariable("id") id: UUID): ResponseEntity<UserDto> {
        return userService.find(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User created"),
            ApiResponse(responseCode = "400", description = "Username already occupied or no body is present")
        ]
    )
    @Operation(description = "Create a user")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun post(@RequestBody createUserCommand: CreateUserCommand): ResponseEntity<UserDto> {
        if (userService.isAlreadyPresent(createUserCommand.username)) {
            return ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(userService.create(createUserCommand).toDto(), HttpStatus.CREATED)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "User updated"),
            ApiResponse(responseCode = "400", description = "Did not find user with id or no body is present")
        ]
    )
    @Operation(description = "Update a user by its id")
    @PutMapping("/update")
    fun put(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        if (userDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val updatedUser = userService.update(
            userModel = UserModel(userDto = userDto)
        )

        return updatedUser?.let { ResponseEntity(it.toDto(), HttpStatus.ACCEPTED) }
            ?: ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ApiResponses(ApiResponse(responseCode = "200", description = "List of usernames found"))
    @GetMapping("/usernames")
    @Operation(description = "Find all usernames for a user type")
    fun findAllUsernames(
        @RequestParam(required = false, defaultValue = "ACTIVE") userType: String
    ): ResponseEntity<List<String>> {
        return ResponseEntity(userService.findUsernames(UserEntity.UserType.valueOf(userType)), HttpStatus.OK)
    }
}
