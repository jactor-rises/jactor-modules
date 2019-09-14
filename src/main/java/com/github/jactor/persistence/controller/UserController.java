package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.command.CreateUserCommandResponse;
import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = UserController.USER, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  static final String USER = "/user";
  private final UserService userServicey;

  @Autowired
  public UserController(UserService userServicey) {
    this.userServicey = userServicey;
  }

  @ApiOperation("Find a user by its username")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User found"),
      @ApiResponse(code = 204, message = "No user with username")
  })
  @GetMapping("/name/{username}")
  public ResponseEntity<UserDto> find(@PathVariable("username") String username) {
    return userServicey.find(username)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @ApiOperation("Get a user by its id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User got"),
      @ApiResponse(code = 404, message = "Did not find user with id")
  })
  @GetMapping("/id/{id}")
  public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
    return userServicey.find(id)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @ApiOperation("Create a user")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "User created")
  })
  @PostMapping("/create")
  public ResponseEntity<CreateUserCommandResponse> post(@RequestBody CreateUserCommand createUserCommand) {
    var primaryKey = userServicey.create(createUserCommand);

    return new ResponseEntity<>(new CreateUserCommandResponse(primaryKey), HttpStatus.CREATED);
  }

  @ApiOperation("Update a user by its id")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User updated"),
      @ApiResponse(code = 400, message = "Did not find user with id")
  })
  @PutMapping("/id/{userId}")
  public ResponseEntity<UserDto> put(@RequestBody UserDto userDto, @PathVariable Long userId) {
    userDto.setId(userId);

    var saved = userServicey.saveOrUpdate(userDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @GetMapping("/active/usernames")
  public ResponseEntity<List<String>> findAllUsernames() {
    return new ResponseEntity<>(userServicey.findUsernamesOnActiveUsers(), HttpStatus.OK);
  }
}
