package io.vitasoft.dvorakbackend.domain.member;

import io.vitasoft.dvorakbackend.domain.member.enums.MemberRole;
import io.vitasoft.dvorakbackend.domain.member.enums.SocialType;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "password", "role"})
@Builder
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String nickname;
    private String imageUrl;
    private int age;
    private String city;

    @Enumerated(EnumType.STRING)
    private MemberRole role; // ROLE_ADMIN, ROLE_USER, ROLE_GUEST

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // FACEBOOK, GOOGLE, KAKAO, NAVER

    @Column(unique = true)
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
