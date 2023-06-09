package io.vitasoft.dvorakbackend.handler.exception;

import io.vitasoft.dvorakbackend.util.ErrorResponse;
import io.vitasoft.dvorakbackend.handler.exception.validation.ValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleBadRequestBody() {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "System-001", "wrong request body");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleRequestValid(MethodArgumentNotValidException exception) {
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            builder.append("[").append(fieldError.getField()).append("](은)는 ").append(fieldError.getDefaultMessage()).append(" 입력된 값: [").append(fieldError.getRejectedValue()).append("]");
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "System-002", builder.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleRequestValid() {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "System-003", "잘못된 데이터 요청");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleMethodNotSupport() {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "System-004", "잘못된 Mapping 요청");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "System-005", "잘못된 parameter 입니다. value: " + exception.getValue());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation-001", "유효성 검사에 실패했습니다. value: " + e.getErrorMap());
    }

}
