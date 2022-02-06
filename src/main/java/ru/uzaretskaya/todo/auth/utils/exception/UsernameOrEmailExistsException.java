package ru.uzaretskaya.todo.auth.utils.exception;

import org.springframework.security.core.AuthenticationException;

public class UsernameOrEmailExistsException extends AuthenticationException {

    public UsernameOrEmailExistsException(String msg) {
        super(msg);
    }

    public UsernameOrEmailExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
