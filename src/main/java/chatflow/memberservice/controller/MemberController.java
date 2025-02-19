package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.member.request.MemberModifyStateRequest;
import chatflow.memberservice.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.dto.member.response.MemberDeleteResponse;
import chatflow.memberservice.dto.member.response.MemberInfoResponse;
import chatflow.memberservice.dto.member.response.MemberModifyStateResponse;
import chatflow.memberservice.dto.member.response.MemberUpdateResponse;
import chatflow.memberservice.entity.member.MemberState;
import chatflow.memberservice.security.MemberAuthorize;
import chatflow.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "회원 API (인증 토큰 필요)")
@RequiredArgsConstructor
@MemberAuthorize
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse<MemberDeleteResponse> deleteMember(@AuthenticationPrincipal User user) {
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping
    public ApiResponse<MemberUpdateResponse> updateMember(@AuthenticationPrincipal User user, @Valid @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "회원 상태 변경")
    @PatchMapping("/status")
    public ApiResponse<MemberModifyStateResponse> modifyMemberState(@AuthenticationPrincipal User user, @Valid @RequestBody MemberModifyStateRequest request) {
        return ApiResponse.success(memberService.modifyMemberState(UUID.fromString(user.getUsername()), request));
    }
}
