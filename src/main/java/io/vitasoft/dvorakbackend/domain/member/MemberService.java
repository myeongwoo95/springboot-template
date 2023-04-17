package io.vitasoft.dvorakbackend.domain.member;

import io.vitasoft.dvorakbackend.domain.member.dto.MemberSignInRequestDto;
import io.vitasoft.dvorakbackend.domain.member.dto.MemberSignupRequestDto;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberAlreadyExistsException;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberNotFoundException;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberPasswordInvalidException;
import io.vitasoft.dvorakbackend.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository userRepository;

    public void signup(MemberSignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new MemberAlreadyExistsException();

        requestDto.updatePassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        userRepository.save(requestDto.toEntity());
    }

    public String signIn(MemberSignInRequestDto requestDto) {
        Member user = userRepository.findByEmail(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new);

        if(!bCryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new MemberPasswordInvalidException();

        return jwtTokenProvider.createAccessToken(user.getEmail());
    }
}
