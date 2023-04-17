package io.vitasoft.dvorakbackend.domain.member.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class MemberSignInResponseDto {
    private String accessToken;
}
