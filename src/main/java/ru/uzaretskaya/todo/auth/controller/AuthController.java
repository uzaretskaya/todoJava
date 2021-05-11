package ru.uzaretskaya.todo.auth.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.exception.UsernameOrEmailExistsException;
import ru.uzaretskaya.todo.auth.object.JsonException;
import ru.uzaretskaya.todo.auth.service.UserService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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

    @PutMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) throws UsernameOrEmailExistsException {
        if (userService.exists(user.getUsername(), user.getEmail())) {
            throw new UsernameOrEmailExistsException("Username or email already exists!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleException(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()), BAD_REQUEST);
    }
}
