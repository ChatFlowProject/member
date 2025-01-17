package chatflow.memberservice.service;

import chatflow.memberservice.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.dto.member.response.MemberDeleteResponse;
import chatflow.memberservice.dto.member.response.MemberInfoResponse;
import chatflow.memberservice.dto.member.response.MemberUpdateResponse;
import chatflow.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID id) {
        return memberRepository.findById(id)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
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
                    return MemberUpdateResponse.of(true, member);
                })
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

//    public MemberInfoResponse getMemberByEmail(String email) {
//        return memberRepository.findByEmail(email)
//                .map(MemberInfoResponse::from)
//                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
//    }

//    public ApiResponse searchMemberByName(String name, int page, int size) {
//        // Pageable 객체 생성 (정렬 기준: 이름 오름차순)
//        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
//
//        // 이름으로 검색 (대소문자 구분 없는 검색을 위해 IgnoreCase 사용)
//        Page<Member> membersPage = memberRepository.findByNameContainingIgnoreCase(name, pageable);
//
//        // Page<Member>를 DTO로 변환
//        return ApiResponse.success(
//                membersPage.getContent().stream()
//                        .map(MemberResponse::fromEntity) // Member -> MemberResponse 변환
//                        .collect(Collectors.toList()),
//                membersPage.getTotalPages(),
//                membersPage.getTotalElements()
//        );
//    }
}
