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
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.user.UserEntity
import com.github.jactor.persistence.user.UserService
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping(path = ["/user"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userService: UserService) {
    @Operation(description = "Find a user by its username")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "204", description = "No user with username")
        ]
    )
    @GetMapping("/name/{username}")
    fun find(@PathVariable("username") username: String): ResponseEntity<UserDto> {
        return userService.find(username = username)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Operation(description = "Get a user by its id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User got"),
            ApiResponse(responseCode = "404", description = "Did not find user with id")
        ]
    )
    @GetMapping("/{id}")
    operator fun get(@PathVariable("id") id: UUID): ResponseEntity<UserDto> {
        return userService.find(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(description = "Create a user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User created"),
            ApiResponse(responseCode = "400", description = "Username already occupied or no body is present")
        ]
    )
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun post(@RequestBody createUserCommand: CreateUserCommand): ResponseEntity<UserDto> {
        if (userService.isAlreadyPresent(createUserCommand.username)) {
            return ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(userService.create(createUserCommand).toDto(), HttpStatus.CREATED)
    }

    @Operation(description = "Update a user by its id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "User updated"),
            ApiResponse(responseCode = "400", description = "Did not find user with id or no body is present")
        ]
    )
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

    @Operation(description = "Find all usernames for a user type")
    @ApiResponses(ApiResponse(responseCode = "200", description = "List of usernames found"))
    @GetMapping("/usernames")
    fun findAllUsernames(
        @RequestParam(required = false, defaultValue = "ACTIVE") userType: String
    ): ResponseEntity<List<String>> {
        return ResponseEntity(userService.findUsernames(UserEntity.UserType.valueOf(userType)), HttpStatus.OK)
    }
}
