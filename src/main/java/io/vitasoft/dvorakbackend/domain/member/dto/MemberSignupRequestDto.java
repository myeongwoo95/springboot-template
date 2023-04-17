package io.vitasoft.dvorakbackend.domain.member.dto;

import io.vitasoft.dvorakbackend.domain.member.Member;
import io.vitasoft.dvorakbackend.domain.member.enums.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class MemberSignupRequestDto {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Size(max = 320, message = "이메일은 320자 이하여야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Min(value = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Size(max = 64, message = "비밀번호는 64자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(max = 15, message = "닉네임은 15자 이하여야 합니다.")
    private String nickname;

    @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150세 이하여야 합니다.")
    private int age;

    @NotBlank(message = "도시는 필수 입력값입니다.")
    @Size(max = 50, message = "도시는 50자 이하여야 합니다.")
    private String city;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .age(age)
                .city(city)
                .role(MemberRole.USER)
                .build();
    }

    public void updatePassword(String password){
        this.password = password;
    }

}
