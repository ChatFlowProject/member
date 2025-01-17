package chatflow.memberservice.dto.sign_in.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(
        @Schema(description = "회원 아이디", example = "jerry0339")
        String account,
        @Schema(description = "회원 비밀번호", example = "4321")
        String password
) {
}
