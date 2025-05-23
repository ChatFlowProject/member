package chatflow.memberservice.presentation.controller.member;

import chatflow.memberservice.presentation.dto.ApiResponse;
import chatflow.memberservice.presentation.dto.member.response.MemberInfoResponse;
import chatflow.memberservice.infrastructure.security.AdminAuthorize;
import chatflow.memberservice.service.member.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "관리자용 API")
@RequiredArgsConstructor
@AdminAuthorize
@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "회원 목록 조회")
    @GetMapping("/members")
    public ApiResponse<List<MemberInfoResponse>> getAllMembers() {
        return ApiResponse.success(adminService.getMembers());
    }

    @Operation(summary = "관리자 목록 조회")
    @GetMapping
    public ApiResponse<List<MemberInfoResponse>> getAllAdmins() {
        return ApiResponse.success(adminService.getAdmins());
    }
}
