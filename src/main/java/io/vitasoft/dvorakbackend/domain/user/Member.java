package io.vitasoft.dvorakbackend.domain.user;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "password", "role"})
@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = MemberRole.ROLE_USER;
    }
}
