package chatflow.memberservice.service;

import chatflow.memberservice.dto.MemberDto;
import chatflow.memberservice.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    MemberDto createMember(MemberDto memberDto);
    MemberDto getMemberByMemberId(String memberId);
    List<Member> getMemberByAll();
    MemberDto getMemberDetailsByEmail(String memberName);
}
