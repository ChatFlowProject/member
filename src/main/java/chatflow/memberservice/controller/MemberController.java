package chatflow.memberservice.controller;

import chatflow.memberservice.dto.MemberDto;
import chatflow.memberservice.entity.Member;
import chatflow.memberservice.service.MemberService;
import chatflow.memberservice.vo.RequestSignUp;
import chatflow.memberservice.vo.ResponseMember;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<ResponseMember> createMember(@RequestBody RequestSignUp member) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        MemberDto memberDto = mapper.map(member, MemberDto.class);
        memberService.createMember(memberDto);

        ResponseMember responseMember = mapper.map(memberDto, ResponseMember.class); // source , destination
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMember);
    }

    @GetMapping("/members")
    public ResponseEntity<List<ResponseMember>> getMembers() {
        ModelMapper mapper = new ModelMapper();
        List<Member> memberList = memberService.getMemberByAll();
        List<ResponseMember> result = memberList.stream()
                .map(member -> mapper.map(member, ResponseMember.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ResponseMember> getMember(@PathVariable("memberId") String memberId) {
        MemberDto memberDto = memberService.getMemberByMemberId(memberId);
        ResponseMember ret = new ModelMapper().map(memberDto, ResponseMember.class);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}
