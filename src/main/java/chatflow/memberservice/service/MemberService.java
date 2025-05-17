package chatflow.memberservice.service;

import chatflow.memberservice.controller.dto.member.request.MemberListRequest;
import chatflow.memberservice.controller.dto.member.request.MemberModifyStateRequest;
import chatflow.memberservice.controller.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.controller.dto.member.response.*;
import chatflow.memberservice.domain.model.Member;
import chatflow.memberservice.domain.model.MemberState;
import chatflow.memberservice.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public MemberDeleteResponse deleteMember(UUID id) {
        if (!memberRepository.existsById(id)) return new MemberDeleteResponse(false);
        memberRepository.deleteById(id);
        return new MemberDeleteResponse(true);
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request) {
        return memberRepository.findById(id)
                .filter(member -> encoder.matches(request.password(), member.getPassword()))
                .map(member -> {
                    member.update(request, encoder);
                    return MemberUpdateResponse.from(true, member);
                })
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    @Transactional
    public MemberModifyStateResponse modifyMemberState(UUID id, MemberModifyStateRequest request) {
        return new MemberModifyStateResponse(getMemberById(id).modifyState(MemberState.of(request.memberState())).toString());
    }
}
