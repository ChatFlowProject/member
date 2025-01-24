package chatflow.memberservice.service;

import chatflow.memberservice.common.MemberType;
import chatflow.memberservice.dto.member.response.MemberInfoResponse;
import chatflow.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getMembers() {
        return memberRepository.findAllByType(MemberType.MEMBER).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getAdmins() {
        return memberRepository.findAllByType(MemberType.ADMIN).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }
}
