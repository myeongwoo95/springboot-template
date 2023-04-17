package io.vitasoft.dvorakbackend.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// should be added "ROLE_" as a prefix.
@Getter
@RequiredArgsConstructor
public enum MemberRole {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private final String key;
}
