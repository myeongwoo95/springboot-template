package io.vitasoft.dvorakbackend.handler.exception;

import io.vitasoft.dvorakbackend.controller.dto.ErrorResponseDto;
import io.vitasoft.dvorakbackend.handler.exception.validation.CustomValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleBadRequestBody() {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "System-001", "wrong request body");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleRequestValid(MethodArgumentNotValidException exception) {
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            builder.append("[").append(fieldError.getField()).append("](은)는 ").append(fieldError.getDefaultMessage()).append(" 입력된 값: [").append(fieldError.getRejectedValue()).append("]");
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "System-002", builder.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleRequestValid() {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "System-003", "잘못된 데이터 요청");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleMethodNotSupport() {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "System-004", "잘못된 Mapping 요청");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "System-005", "잘못된 parameter 입니다. value: " + exception.getValue());
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleCustomValidationException(CustomValidationException e) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, "Validation-001", "유효성 검사에 실패했습니다. value: " + e.getErrorMap());
    }

}
