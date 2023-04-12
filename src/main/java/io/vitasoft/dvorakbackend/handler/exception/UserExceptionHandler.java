package io.vitasoft.dvorakbackend.handler.exception;

import io.vitasoft.dvorakbackend.controller.dto.ErrorResponseDto;
import io.vitasoft.dvorakbackend.handler.exception.user.UserAlreadyExistsException;
import io.vitasoft.dvorakbackend.handler.exception.user.UserNotFoundException;
import io.vitasoft.dvorakbackend.handler.exception.user.UserPasswordInvalidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return new ErrorResponseDto(HttpStatus.CONFLICT, "Auth-001", exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNotFound(UserNotFoundException exception) {
        return new ErrorResponseDto(HttpStatus.NOT_FOUND, "Auth-002", exception.getMessage());
    }

    @ExceptionHandler(UserPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUserPasswordInvalidException(UserPasswordInvalidException exception) {
        return new ErrorResponseDto(HttpStatus.UNAUTHORIZED, "Auth-003", exception.getMessage());
    }
}
