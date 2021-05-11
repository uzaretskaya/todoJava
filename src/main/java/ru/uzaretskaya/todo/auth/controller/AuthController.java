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
import ru.uzaretskaya.todo.auth.entity.Role;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.exception.RoleNotFoundException;
import ru.uzaretskaya.todo.auth.exception.UsernameOrEmailExistsException;
import ru.uzaretskaya.todo.auth.object.JsonException;
import ru.uzaretskaya.todo.auth.service.UserService;

import javax.validation.Valid;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.uzaretskaya.todo.auth.service.UserService.DEFAULT_ROLE;

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
    public ResponseEntity<User> register(@Valid @RequestBody User user) throws UsernameOrEmailExistsException, RoleNotFoundException {
        if (userService.exists(user.getUsername(), user.getEmail())) {
            throw new UsernameOrEmailExistsException("Username or email already exists!");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        Role userRole = userService.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException(format("Default role %s not found!", DEFAULT_ROLE)));
        user.getRoles().add(userRole);

        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleException(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()), BAD_REQUEST);
    }
}
