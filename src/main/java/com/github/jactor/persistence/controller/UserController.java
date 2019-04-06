package com.github.jactor.persistence.controller;

import com.github.jactor.persistence.dto.UserDto;
import com.github.jactor.persistence.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USER)
public class UserController {

  static final String USER = "/user";
  private final UserService userServicey;

  @Autowired
  public UserController(UserService userServicey) {
    this.userServicey = userServicey;
  }

  @GetMapping("/name/{username}")
  public ResponseEntity<UserDto> find(@PathVariable("username") String username) {
    return userServicey.find(username)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
    return userServicey.find(id)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @PostMapping
  public ResponseEntity<UserDto> post(@RequestBody UserDto userDto) {
    if (userDto.getId() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    UserDto saved = userServicey.saveOrUpdate(userDto);

    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UserDto> put(@RequestBody UserDto userDto, @PathVariable Long userId) {
    if (userDto.getId() == null || !userDto.getId().equals(userId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    UserDto saved = userServicey.saveOrUpdate(userDto);

    return new ResponseEntity<>(saved, HttpStatus.ACCEPTED);
  }

  @GetMapping("/active/usernames")
  public ResponseEntity<List<String>> findAllUsernames() {
    return new ResponseEntity<>(userServicey.findUsernamesOnActiveUsers(), HttpStatus.OK);
  }
}
