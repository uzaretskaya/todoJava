package ru.uzaretskaya.todo.auth.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.auth.entity.Activity;
import ru.uzaretskaya.todo.auth.entity.Role;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.exception.RoleNotFoundException;
import ru.uzaretskaya.todo.auth.exception.UserAlreadyActivatedException;
import ru.uzaretskaya.todo.auth.exception.UsernameOrEmailExistsException;
import ru.uzaretskaya.todo.auth.object.JsonException;
import ru.uzaretskaya.todo.auth.service.UserDetailsImpl;
import ru.uzaretskaya.todo.auth.service.UserService;
import ru.uzaretskaya.todo.auth.utils.CookieUtils;
import ru.uzaretskaya.todo.auth.utils.JwtUtils;

import javax.validation.Valid;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.uzaretskaya.todo.auth.service.UserService.DEFAULT_ROLE;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {
    private UserService userService;
    private PasswordEncoder encoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    @Autowired
    public AuthController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            CookieUtils cookieUtils
    ) {
        this.userService = userService;
        this.encoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
    }

    @GetMapping("/test")
    @CrossOrigin(origins = "*")
    public String test() {
        return "OK";
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

        Activity activity = new Activity();
        activity.setUser(user);
        activity.setUuid(UUID.randomUUID().toString());
        userService.register(user, activity);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {

        Activity activity = userService.findActivityByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException(format("Activity with uuid: %s not found!", uuid)));

        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }

        int updatedCount = userService.activate(uuid);

        return ResponseEntity.ok(updatedCount == 1);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.isActivated()) {
            throw new DisabledException("User disabled");
        }

        String jwt = jwtUtils.createAccessToken(userDetails.getUser());

        userDetails.getUser().setPassword(null);

        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleException(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName()), BAD_REQUEST);
    }
}
