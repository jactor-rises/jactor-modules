package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.command.CreateUserCommand;
import com.github.jactor.persistence.command.CreateUserCommandResponse;
import com.github.jactor.persistence.dto.UserInternalDto;
import com.github.jactor.persistence.entity.UserEntity.UserType;
import com.github.jactor.persistence.service.UserService;
import com.github.jactor.shared.dto.CreateUserCommandDto;
import com.github.jactor.shared.dto.UserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.constraints.NotNull;
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

  @ApiOperation("Find a user by its username")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User found"),
      @ApiResponse(code = 204, message = "No user with username")
  })
  @GetMapping("/name/{username}")
  public ResponseEntity<UserDto> find(@PathVariable("username") String username) {
    return userServicey.find(username)
        .map(UserInternalDto::toUserDto)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @ApiOperation("Get a user by its id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User got"),
      @ApiResponse(code = 404, message = "Did not find user with id")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
    return userServicey.find(id)
        .map(UserInternalDto::toUserDto)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @ApiOperation("Create a user")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "User created"),
      @ApiResponse(code = 400, message = "Username already occupied or no body is present")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateUserCommandResponse> post(@NotNull @RequestBody CreateUserCommandDto createUserCommand) {
    if (userServicey.isAllreadyPresent(createUserCommand.getUsername())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    var primaryKey = userServicey.create(new CreateUserCommand(createUserCommand));

    return new ResponseEntity<>(new CreateUserCommandResponse(primaryKey), HttpStatus.CREATED);
  }

  @ApiOperation("Update a user by its id")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User updated"),
      @ApiResponse(code = 400, message = "Did not find user with id or no body is present")
  })
  @PutMapping("/{userId}")
  public ResponseEntity<UserDto> put(@NotNull @RequestBody UserDto userDto, @PathVariable Long userId) {
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
