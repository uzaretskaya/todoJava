package ru.uzaretskaya.todo.auth.utils.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyActivatedException extends AuthenticationException {

    public UserAlreadyActivatedException(String msg) {
        super(msg);
    }

    public UserAlreadyActivatedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
