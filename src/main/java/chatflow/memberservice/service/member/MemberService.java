package chatflow.memberservice.service.member;

import chatflow.memberservice.infrastructure.outbox.event.member.MemberDeleteEvent;
import chatflow.memberservice.infrastructure.outbox.event.member.MemberModifyStatusEvent;
import chatflow.memberservice.infrastructure.outbox.event.member.MemberUpdateEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;
import chatflow.memberservice.presentation.dto.member.request.MemberListRequest;
import chatflow.memberservice.presentation.dto.member.request.MemberModifyStateRequest;
import chatflow.memberservice.presentation.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.presentation.dto.member.response.*;
import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.domain.member.MemberState;
import chatflow.memberservice.infrastructure.repository.member.MemberRepository;
import chatflow.memberservice.common.exception.custom.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public Member getMemberById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID id) {
        return MemberInfoResponse.from(getMemberById(id));
    }

    @Transactional(readOnly = true)
    public MemberSimpleResponse getMemberSimpleInfo(UUID id) {
        return MemberSimpleResponse.from(getMemberById(id));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfoList(UUID id, MemberListRequest request) {
        return MemberResponse.from(id, memberRepository.findByIdIn(request.memberIds()).stream()
                .map(MemberSimpleResponse::from)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteMember(UUID id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeleteEvent(member.getId().toString(), MemberEventPayload.from(member)));
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request) {
        Member member = getMemberById(id);
        if (!encoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        member.update(request, encoder);
        eventPublisher.publishEvent(new MemberUpdateEvent(member.getId().toString(), MemberEventPayload.from(member)));
        return MemberUpdateResponse.from(member);
    }

    @Transactional
    public MemberModifyStateResponse modifyMemberState(UUID id, MemberModifyStateRequest request) {
        Member member = getMemberById(id);
        MemberState memberState = member.modifyState(MemberState.of(request.memberState()));
        eventPublisher.publishEvent(new MemberModifyStatusEvent(member.getId().toString(), MemberEventPayload.from(member)));
        return new MemberModifyStateResponse(memberState.toString());
    }

}
