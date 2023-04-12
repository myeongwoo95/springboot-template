package io.vitasoft.dvorakbackend.controller;

import io.swagger.annotations.ApiOperation;
import io.vitasoft.dvorakbackend.controller.dto.user.*;
import io.vitasoft.dvorakbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/api/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        userService.signup(userSignupRequestDto);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/api/auth/signin")
    @ResponseStatus(HttpStatus.OK)
    public UserSignInResponseDto signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        String accessToken = userService.signIn(userSignInRequestDto);
        return new UserSignInResponseDto(accessToken);
    }


}
