package io.vitasoft.dvorakbackend.handler.exception.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("해당 사용자가 존재하지 않습니다");
    }
}
