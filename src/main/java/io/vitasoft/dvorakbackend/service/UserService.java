package io.vitasoft.dvorakbackend.service;

import io.vitasoft.dvorakbackend.controller.dto.user.UserSignInRequestDto;
import io.vitasoft.dvorakbackend.controller.dto.user.UserSignupRequestDto;
import io.vitasoft.dvorakbackend.domain.user.User;
import io.vitasoft.dvorakbackend.domain.user.UserRepository;
import io.vitasoft.dvorakbackend.handler.exception.user.UserAlreadyExistsException;
import io.vitasoft.dvorakbackend.handler.exception.user.UserNotFoundException;
import io.vitasoft.dvorakbackend.handler.exception.user.UserPasswordInvalidException;
import io.vitasoft.dvorakbackend.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public void signup(UserSignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new UserAlreadyExistsException();

        requestDto.updatePassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        userRepository.save(requestDto.toEntity());
    }

    public String signIn(UserSignInRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(UserNotFoundException::new);

        if(!bCryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new UserPasswordInvalidException();

        return jwtTokenProvider.generateJwtToken(user.getId(), user.getRole());
    }


}
