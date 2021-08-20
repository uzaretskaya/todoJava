package ru.uzaretskaya.todo.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtCommonException extends AuthenticationException {

    public JwtCommonException(String msg) {
        super(msg);
    }

    public JwtCommonException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
