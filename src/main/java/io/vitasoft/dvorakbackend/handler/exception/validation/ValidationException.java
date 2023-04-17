package io.vitasoft.dvorakbackend.handler.exception.validation;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Map<String, String> errorMap;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap(){
        return errorMap;
    }

}
