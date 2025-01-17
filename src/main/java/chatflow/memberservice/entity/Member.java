package chatflow.memberservice.entity;

import chatflow.memberservice.common.MemberType;
import chatflow.memberservice.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.dto.sign_up.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // memberId
    @Column(nullable = false, scale = 20, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private String name;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private MemberState state;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @CreationTimestamp
    private LocalDateTime createdAt;


    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .state(MemberState.OFFLINE)
                .type(MemberType.USER)
                .build();
    }

    @Builder
    private Member(String account, String password, String name, String email, Integer age, MemberState state,MemberType type) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.email = email;
        this.age = age;
        this.state = state;
        this.type = type;
    }

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.name = newMember.name();
        this.age = newMember.age();
    }
}
