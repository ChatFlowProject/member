package chatflow.memberservice;

import chatflow.memberservice.common.MemberType;
import chatflow.memberservice.entity.Member;
import chatflow.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
        memberRepository.save(Member.builder()
                .account("admin")
                .password(encoder.encode("admin"))
                .name("관리자")
                .email("jcu011@naver.com")
                .type(MemberType.ADMIN)
                .build());
    }
}
