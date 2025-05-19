package chatflow.memberservice.presentation.dto.sign_in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Schema(description = "회원 이메일", example = "jerry0339@naver.com")
        String email,
        @NotBlank(message = "비밀 번호를 입력해 주세요.")
        @Schema(description = "회원 비밀번호", example = "4321")
        String password
) {
}
