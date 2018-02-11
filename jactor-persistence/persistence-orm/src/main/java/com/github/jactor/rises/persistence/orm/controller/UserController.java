package com.github.jactor.rises.persistence.orm.controller;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.persistence.orm.service.UserService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    private final UserService userServicey;

    @Autowired
    public UserController(UserService userServicey) {
        this.userServicey = userServicey;
    }

    @GetMapping("/find/{userName}")
    public ResponseEntity<UserDto> find(@PathVariable("userName") String userName) {
        Optional<UserDto> possibleUser = userServicey.findUsing(new UserName(userName));

        return possibleUser.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServicey.fetch(id), HttpStatus.OK);
    }

    @PostMapping("/persist")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto saved = userServicey.saveOrUpdate(userDto);

        if (userDto.getId() == null) {
            return createdWithLocation(saved);
        }

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    private ResponseEntity<UserDto> createdWithLocation(UserDto saved) {
        try {
            return ResponseEntity.created(new URI(null, null, "/user/get/" + saved.getId(), null)).body(saved);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
