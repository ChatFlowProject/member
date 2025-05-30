package chatflow.memberservice.service;

import chatflow.memberservice.domain.member.MemberType;
import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.infrastructure.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
        if(memberRepository.findByEmail("admin").isPresent()) return;
        memberRepository.save(Member.builder()
                .email("admin")
                .password(encoder.encode("admin"))
                .nickname("admin")
                .name("관리자")
                .birth(LocalDate.of(2025, 1, 1))
                .type(MemberType.ADMIN)
                .build());
    }
}
