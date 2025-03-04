package chatflow.memberservice.service;

import chatflow.memberservice.dto.sign_in.SignInRequest;
import chatflow.memberservice.dto.sign_in.SignInResponse;
import chatflow.memberservice.dto.sign_up.SignUpRequest;
import chatflow.memberservice.dto.sign_up.SignUpResponse;
import chatflow.memberservice.entity.member.Member;
import chatflow.memberservice.repository.MemberRepository;
import chatflow.memberservice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;

    @Transactional
    public SignUpResponse registMember(SignUpRequest request) {
        Member member = memberRepository.save(Member.create(request, encoder));
        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(request.email()))
                throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
            if (e.getMessage().contains(request.nickname()))
                throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
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
