package io.vitasoft.dvorakbackend.handler.exception;

import io.vitasoft.dvorakbackend.controller.dto.ErrorResponseDto;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberAlreadyExistsException;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberNotFoundException;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberPasswordInvalidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserAlreadyExists(MemberAlreadyExistsException exception) {
        return new ErrorResponseDto(HttpStatus.CONFLICT, "Auth-001", exception.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNotFound(MemberNotFoundException exception) {
        return new ErrorResponseDto(HttpStatus.NOT_FOUND, "Auth-002", exception.getMessage());
    }

    @ExceptionHandler(MemberPasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUserPasswordInvalidException(MemberPasswordInvalidException exception) {
        return new ErrorResponseDto(HttpStatus.UNAUTHORIZED, "Auth-003", exception.getMessage());
    }
}
