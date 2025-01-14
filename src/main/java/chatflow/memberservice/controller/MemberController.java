package chatflow.memberservice.controller;

import chatflow.memberservice.dto.MemberDto;
import chatflow.memberservice.service.MemberService;
import chatflow.memberservice.vo.RequestSignIn;
import chatflow.memberservice.vo.RequestSignUp;
import chatflow.memberservice.vo.ResponseMember;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MemberController {
    private final Environment env;
    private final MemberService memberService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("Working in Member Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseMember> createMember(@RequestBody RequestSignUp member) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        MemberDto memberDto = mapper.map(member, MemberDto.class);
        memberService.createMember(memberDto);

        ResponseMember responseMember = mapper.map(memberDto, ResponseMember.class); // source , destination
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMember);
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user credentials and return a token")
    public String login(@RequestBody RequestSignIn requestSignIn) {
        return "This endpoint is handled by Spring Security.";
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ResponseMember> getMemberByMemberId(@PathVariable("memberId") String memberId) {
        MemberDto memberDto = memberService.getMemberByMemberId(memberId);
        ResponseMember ret = new ModelMapper().map(memberDto, ResponseMember.class);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @GetMapping("/members")
    public ResponseEntity<ResponseMember> getMemberByEmail(@RequestParam("email") String email) {
        MemberDto memberDto = memberService.getMemberByEmail(email);
        ResponseMember ret = new ModelMapper().map(memberDto, ResponseMember.class);

        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}
