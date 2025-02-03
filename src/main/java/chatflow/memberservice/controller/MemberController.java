package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.security.MemberAuthorize;
import chatflow.memberservice.service.AdminService;
import chatflow.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "로그인 후 사용할 수 있는 API")
@RequiredArgsConstructor
@MemberAuthorize
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final AdminService adminService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping
    public ApiResponse updateMember(@AuthenticationPrincipal User user, @Valid @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "회원 전체 목록 조회 (친구 서비스 생성 전 임시 API)")
    @GetMapping("/friends")
    public ApiResponse getAllMembers() {
        return ApiResponse.success(adminService.getMembers());
    }
}
