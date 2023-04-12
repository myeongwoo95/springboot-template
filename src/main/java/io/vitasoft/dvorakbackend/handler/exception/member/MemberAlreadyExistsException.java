package io.vitasoft.dvorakbackend.handler.exception.member;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException() {
        super("이미 존재하는 아이디입니다.");
    }
}
