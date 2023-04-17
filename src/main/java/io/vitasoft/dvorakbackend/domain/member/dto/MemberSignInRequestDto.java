package io.vitasoft.dvorakbackend.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberSignInRequestDto {

    @NotNull(message = "사용자명은 필수 입력값입니다.")
    @Size(max = 30, message = "사용자명은 30자 이하여야 합니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(max = 30, message = "비밀번호는 30자 이하여야 합니다.")
    private String password;
}
