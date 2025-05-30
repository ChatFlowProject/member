package chatflow.memberservice.service.member;

import chatflow.memberservice.infrastructure.outbox.event.member.SignUpEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;
import chatflow.memberservice.presentation.dto.sign_in.SignInRequest;
import chatflow.memberservice.presentation.dto.sign_in.SignInResponse;
import chatflow.memberservice.presentation.dto.sign_up.SignUpRequest;
import chatflow.memberservice.presentation.dto.sign_up.SignUpResponse;
import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.infrastructure.repository.member.MemberRepository;
import chatflow.memberservice.infrastructure.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;

    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        Member member = Member.create(request, encoder);
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(request.email()))
                throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
            if (e.getMessage().contains(request.nickname()))
                throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        eventPublisher.publishEvent(new SignUpEvent(member.getId().toString(), MemberEventPayload.from(member)));
        return SignUpResponse.from(member);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .filter(it -> encoder.matches(request.password(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        String token = tokenProvider.createToken(String.format("%s:%s", member.getId(), member.getType()));
        return new SignInResponse(member.getId(), member.getName(), member.getType(), token);
    }
}
