package chatflow.memberservice.presentation.controller.member;

import chatflow.memberservice.presentation.dto.ApiResponse;
import chatflow.memberservice.presentation.dto.sign_in.SignInRequest;
import chatflow.memberservice.presentation.dto.sign_in.SignInResponse;
import chatflow.memberservice.presentation.dto.sign_up.SignUpRequest;
import chatflow.memberservice.presentation.dto.sign_up.SignUpResponse;
import chatflow.memberservice.service.member.SignService;
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
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ApiResponse.success(signService.signIn(request));
    }
}
