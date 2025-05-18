package chatflow.memberservice.service.member;

import chatflow.memberservice.controller.dto.member.request.MemberModifyStateRequest;
import chatflow.memberservice.controller.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.controller.dto.member.response.MemberModifyStateResponse;
import chatflow.memberservice.controller.dto.member.response.MemberUpdateResponse;
import chatflow.memberservice.domain.event.member.MemberDeleteEvent;
import chatflow.memberservice.domain.event.member.MemberModifyStatusEvent;
import chatflow.memberservice.domain.event.member.MemberUpdateEvent;
import chatflow.memberservice.domain.model.member.Member;
import chatflow.memberservice.domain.model.member.MemberState;
import chatflow.memberservice.domain.repository.MemberRepository;
import chatflow.memberservice.exception.custom.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceV2 {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public Member getMemberById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request) {
        Member member = getMemberById(id);
        if(!encoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        member.update(request, encoder);
        eventPublisher.publishEvent(new MemberUpdateEvent(member.getId().toString(), member));
        return MemberUpdateResponse.from(member);
    }

    @Transactional
    public MemberModifyStateResponse modifyMemberState(UUID id, MemberModifyStateRequest request) {
        Member member = getMemberById(id);
        MemberState memberState = member.modifyState(MemberState.of(request.memberState()));
        eventPublisher.publishEvent(new MemberModifyStatusEvent(member.getId().toString(), member));
        return new MemberModifyStateResponse(memberState.toString());
    }

    @Transactional
    public void deleteMember(UUID id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeleteEvent(member.getId().toString(), member));
    }

}
