package com.github.jactorrises.persistence.orm.controller;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.persistence.dto.UserDto;
import com.github.jactorrises.persistence.orm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userServicey;

    @Autowired
    public UserController(UserService userServicey) {
        this.userServicey = userServicey;
    }

    @GetMapping("/{userName}")
    public UserDto find(@PathVariable("userName") String userName) {
        return userServicey.findUsing(new UserName(userName)).orElse(null);
    }
}
