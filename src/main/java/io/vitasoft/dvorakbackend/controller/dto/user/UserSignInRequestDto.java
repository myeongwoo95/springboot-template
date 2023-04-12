package io.vitasoft.dvorakbackend.controller.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserSignInRequestDto {

    @NotNull(message = "사용자명은 필수 입력값입니다.")
    @Size(max = 30, message = "사용자명은 30자 이하여야 합니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(max = 30, message = "비밀번호는 30자 이하여야 합니다.")
    private String password;
}
