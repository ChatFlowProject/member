package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.sign_in.SignInRequest;
import chatflow.memberservice.dto.sign_in.SignInResponse;
import chatflow.memberservice.dto.sign_up.SignUpRequest;
import chatflow.memberservice.dto.sign_up.SignUpResponse;
import chatflow.memberservice.service.SignService;
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
@RequestMapping
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ApiResponse.success(signService.signIn(request));
    }
}
