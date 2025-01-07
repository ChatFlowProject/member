package chatflow.memberservice.service;

import chatflow.memberservice.dto.MemberDto;
import chatflow.memberservice.entity.Member;
import chatflow.memberservice.entity.MemberState;
import chatflow.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberDto createMember(MemberDto memberDto) {
        memberDto.setMemberId(UUID.randomUUID().toString());
        memberDto.setMemberState(MemberState.ONLINE);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = mapper.map(memberDto, Member.class);
        member.setEncryptedPwd(passwordEncoder.encode(memberDto.getPwd()));

        memberRepository.save(member);

        MemberDto retMemberDto = mapper.map(member, MemberDto.class);

        return retMemberDto;
    }

    @Override
    public List<Member> getMemberByAll() {
        return memberRepository.findAll();
    }

    @Override // `UserDetailsService`의 abstract method
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(memberName)
                .orElseThrow(() -> new UsernameNotFoundException(memberName + ": not found"));

        log.debug(member.getEmail() + " is loaded.");

        // 아래의 User는 Spring Security에서 제공하는 User 클래스임
        return new User(
                member.getEmail(),
                member.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>() // 로그인 되었을 때 부여할 권한 List
        );
    }

    @Override
    public MemberDto getMemberByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        log.debug("{} is found by uuid.", member.getEmail());
        MemberDto memberDto = new ModelMapper().map(member, MemberDto.class);

        return memberDto;
    }

    @Override
    public MemberDto getMemberByEmail(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException(memberEmail));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(member, MemberDto.class);
    }
}
