package com.hes.account.controller;

import com.hes.account.exception.AccountException;
import com.hes.account.model.User;
import com.hes.account.service.JwtServiceImpl;
import com.hes.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    public static final String AUTHORIZATION = "Authorization";
    @Autowired
    private UserService userService;
    @Autowired
    private JwtServiceImpl jwtService;

    @PostMapping(path = "login", consumes = "application/json")
    ResponseEntity<User> login(@RequestBody User user) throws AccountException {

        User storedUser = userService.login(user);
        String token = jwtService.generateToken(storedUser);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, token)
                .body(storedUser);
    }
}
