package chatflow.memberservice.presentation.controller.member;

import chatflow.memberservice.presentation.dto.ApiResponse;
import chatflow.memberservice.presentation.dto.sign_up.SignUpRequest;
import chatflow.memberservice.presentation.dto.sign_up.SignUpResponse;
import chatflow.memberservice.service.member.SignServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 가입 및 로그인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v2")
public class SignControllerV2 {
    private final SignServiceV2 signService;

    @Operation(summary = "회원 가입 V2")
    @PostMapping("/sign-up")
    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

}
