package chatflow.memberservice.controller.member;

import chatflow.memberservice.controller.dto.ApiResponse;
import chatflow.memberservice.controller.dto.member.request.MemberModifyStateRequest;
import chatflow.memberservice.controller.dto.member.request.MemberUpdateRequest;
import chatflow.memberservice.controller.dto.member.response.*;
import chatflow.memberservice.infrastructure.security.MemberAuthorize;
import chatflow.memberservice.service.member.MemberServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "회원 API (인증 토큰 필요)")
@RequiredArgsConstructor
@MemberAuthorize
@RestController
@RequestMapping("/v2/members")
@Slf4j
public class MemberControllerV2 {
    private final MemberServiceV2 memberService;

    @Operation(summary = "나의 정보 수정 v2")
    @PutMapping
    public ApiResponse<MemberUpdateResponse> updateMember(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "나의 상태 변경 v2")
    @PatchMapping("/status")
    public ApiResponse<MemberModifyStateResponse> modifyMemberState(@AuthenticationPrincipal User user, @Valid @RequestBody MemberModifyStateRequest request) {
        return ApiResponse.success(memberService.modifyMemberState(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "나의 회원 탈퇴 v2")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal User user) {
        memberService.deleteMember(UUID.fromString(user.getUsername()));
        return ApiResponse.success();
    }
}
