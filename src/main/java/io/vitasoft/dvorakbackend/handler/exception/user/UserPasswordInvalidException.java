package io.vitasoft.dvorakbackend.handler.exception.user;

public class UserPasswordInvalidException extends RuntimeException {
    public UserPasswordInvalidException() {
        super("비밀번호가 틀렸습니다.");
    }
}
