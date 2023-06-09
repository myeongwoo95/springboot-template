package io.vitasoft.dvorakbackend.domain.member;

import io.swagger.annotations.ApiOperation;
import io.vitasoft.dvorakbackend.domain.member.MemberService;
import io.vitasoft.dvorakbackend.domain.member.dto.MemberSignInRequestDto;
import io.vitasoft.dvorakbackend.domain.member.dto.MemberSignInResponseDto;
import io.vitasoft.dvorakbackend.domain.member.dto.MemberSignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/api/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody MemberSignupRequestDto userSignupRequestDto) {
        userService.signup(userSignupRequestDto);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/api/auth/signin")
    @ResponseStatus(HttpStatus.OK)
    public MemberSignInResponseDto signIn(@RequestBody MemberSignInRequestDto userSignInRequestDto, BindingResult bindingResult) {
        String accessToken = userService.signIn(userSignInRequestDto);
        return new MemberSignInResponseDto(accessToken);
    }


}
