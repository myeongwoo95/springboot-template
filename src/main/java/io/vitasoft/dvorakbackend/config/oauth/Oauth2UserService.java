package io.vitasoft.dvorakbackend.config.oauth;

import io.vitasoft.dvorakbackend.config.oauth.provider.*;
import io.vitasoft.dvorakbackend.domain.user.Member;
import io.vitasoft.dvorakbackend.domain.user.MemberRepository;
import io.vitasoft.dvorakbackend.domain.user.MemberRole;
import io.vitasoft.dvorakbackend.handler.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. OAuth 로그인 요청 -> 로그인 페이지 제공(ID, PW 요청) -> ID, PW 입력
        // 2. Provider에서 Authorization Code를 발급 (Redirect: Callback URL, OAuth-client 라이브러리가 알아서 받아줌)
        // 3. OAuth-client 라이브러리가 code를 이용해서 Access Token을 요청 -> Access Token 발급 -> Token 저장 -> Access Token으로 API 호출
        // 4. userReuqest 정보 -> loadUser함수 호출 -> Provider로부터 회원정보를 받음
        // Tip. OAuth 라이브러리를 사용하면 Authorization Code 알아서 받고 그걸로 액세스토큰 + 사용자 프로필 정보를 받는다.

        log.info("getClientRegistration() = " + userRequest.getClientRegistration()); // ClientRegistration로 OAuth 구분 ex) google, facebook
        log.info("getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue()); // 액세스토큰
        log.info("getAttributes() = " + super.loadUser(userRequest).getAttributes()); // 사용자 정보 (Provider에 따라 양식이 다름, 네이버나 카카오톡은 Provider X)

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        switch(userRequest.getClientRegistration().getRegistrationId()) {
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "facebook":
                oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
                break;
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
            case "naver":
                oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
                break;
        }

        String provider = oAuth2UserInfo.getProvider(); // google, facebook
        String providerId = oAuth2UserInfo.getProviderId(); // google = "sub", facebook = "id"
        String username = provider + "_" + providerId; // google_12312738912379
        String password = bCryptPasswordEncoder.encode("username");
        String email = oAuth2UserInfo.getEmail();

        boolean existsByUsername = memberRepository.existsByUsername(username); // 변수명 머냥...

        if(!existsByUsername){
            Member member = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();

            memberRepository.save(member);
        }

        //return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
        return null;
    }
}
