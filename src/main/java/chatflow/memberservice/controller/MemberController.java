package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.security.UserAuthorize;
import chatflow.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "로그인 후 사용할 수 있는 API")
@RequiredArgsConstructor
@UserAuthorize
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }

//    @GetMapping("/email/{email}")
//    public ApiResponse getMemberByEmail(@PathVariable("email") String email) {
//        return ApiResponse.success(memberService.getMemberByEmail(email));
//    }
//
//    @GetMapping("/search")
//    public ApiResponse searchMemberByName(
//            @RequestParam String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return ApiResponse.success(memberService.getAllMemberByName(name, page, size));
//    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping
    public ApiResponse updateMember(@AuthenticationPrincipal User user, @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername()), request));
    }
}
