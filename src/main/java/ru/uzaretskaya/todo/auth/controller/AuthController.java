package ru.uzaretskaya.todo.auth.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {

    private UserService userService;
    private PasswordEncoder encoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.encoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok().build();
    }
}