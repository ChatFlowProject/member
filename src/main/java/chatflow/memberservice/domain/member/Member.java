package chatflow.memberservice.domain.member;

import chatflow.memberservice.presentation.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.presentation.dto.sign_up.SignUpRequest;
import chatflow.memberservice.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 20, unique = true)
    private String nickname;
    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @Enumerated(EnumType.STRING)
    private MemberState state;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    private String avatarUrl;

    @Builder
    private Member(String email, String password, String nickname, String name, LocalDate birth, MemberState state, MemberType type, String avatarUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.state = state;
        this.type = type;
        this.avatarUrl = avatarUrl;
    }

    public static Member create(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .nickname(request.nickname())
                .name(request.name())
                .birth(LocalDate.parse(request.birth(), DateTimeFormatter.ISO_LOCAL_DATE))
                .state(MemberState.OFFLINE)
                .type(MemberType.MEMBER)
                .avatarUrl("")
                .build();
    }

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.name = newMember.name();
        this.birth = LocalDate.parse(newMember.birth(), DateTimeFormatter.ISO_LOCAL_DATE);
        this.avatarUrl = newMember.avatarUrl();
    }

    public MemberState modifyState(MemberState state) {
        return this.state = state;
    }

}
