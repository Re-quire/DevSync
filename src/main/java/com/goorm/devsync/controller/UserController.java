package com.goorm.devsync.controller;

import com.goorm.devsync.domain.user.User;
import com.goorm.devsync.service.UserSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserSerivce userSerivce;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userSerivce.createUser(user.getName());
    }
}
