package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.command.CreateUserCommandResponse;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.UserEntity.UserType;
import com.github.jactor.persistence.service.UserService;
import com.github.jactor.shared.dto.CreateUserCommandDto;
import com.github.jactor.shared.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = UserController.USER, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  static final String USER = "/user";
  private final UserService userServicey;

  @Autowired
  public UserController(UserService userServicey) {
    this.userServicey = userServicey;
  }

  @Operation(description = "Find a user by its username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "204", description = "No user with username")
  })
  @GetMapping("/name/{username}")
  public ResponseEntity<UserDto> find(@PathVariable("username") String username) {
    return userServicey.find(username)
        .map(UserInternalDto::toUserDto)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @Operation(description ="Get a user by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User got"),
      @ApiResponse(responseCode = "404", description = "Did not find user with id")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
    return userServicey.find(id)
        .map(UserInternalDto::toUserDto)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(description ="Create a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User created"),
      @ApiResponse(responseCode = "400", description = "Username already occupied or no body is present")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateUserCommandResponse> post(@RequestBody CreateUserCommandDto createUserCommand) {
    if (userServicey.isAllreadyPresent(createUserCommand.getUsername())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    var primaryKey = userServicey.create(new CreateUserCommand(createUserCommand));

    return new ResponseEntity<>(new CreateUserCommandResponse(primaryKey), HttpStatus.CREATED);
  }

  @Operation(description ="Update a user by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "User updated"),
      @ApiResponse(responseCode = "400", description = "Did not find user with id or no body is present")
  })
  @PutMapping("/{userId}")
  public ResponseEntity<UserDto> put(@RequestBody UserDto userDto, @PathVariable Long userId) {
    userDto.setId(userId);

    return userServicey.update(new UserInternalDto(userDto))
        .map(UserInternalDto::toUserDto)
        .map(user -> new ResponseEntity<>(user, HttpStatus.ACCEPTED))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @GetMapping("/usernames")
  public ResponseEntity<List<String>> findAllUsernames(@RequestParam(required = false, defaultValue = "ACTIVE") String userType) {
    return new ResponseEntity<>(userServicey.findUsernames(UserType.valueOf(userType)), HttpStatus.OK);
  }
}
