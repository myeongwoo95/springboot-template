package io.vitasoft.dvorakbackend.controller.dto.user;

import lombok.*;

@Getter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String accessToken;
}
