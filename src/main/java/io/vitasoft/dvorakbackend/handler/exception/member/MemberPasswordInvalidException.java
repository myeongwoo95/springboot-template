package io.vitasoft.dvorakbackend.handler.exception.member;

public class MemberPasswordInvalidException extends RuntimeException {
    public MemberPasswordInvalidException() {
        super("비밀번호가 틀렸습니다.");
    }
}
