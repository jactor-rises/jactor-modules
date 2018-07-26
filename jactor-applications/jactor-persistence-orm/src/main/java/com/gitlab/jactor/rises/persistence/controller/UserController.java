package com.gitlab.jactor.rises.persistence.controller;

import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends AbstractController {

    private final UserService userServicey;

    @Autowired
    public UserController(UserService userServicey) {
        this.userServicey = userServicey;
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> find(@PathVariable("username") String username) {
        return userServicey.find(username)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
        return userServicey.find(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping("/persist")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto saved = userServicey.saveOrUpdate(userDto);

        if (userDto.getId() == null) {
            return aCreatedResponseEntity(saved, "/user/get/" + saved.getId());
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/all/usernames")
    public ResponseEntity<List<String>> findAllUsernames() {
        return new ResponseEntity<>(userServicey.findUsernamesOnActiveUsers(), HttpStatus.OK);
    }
}
