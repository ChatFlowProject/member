package chatflow.memberservice.dto.sign_up.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpRequest(
        @Schema(description = "회원 아이디", example = "jerry0339")
        String account,
        @Schema(description = "회원 비밀번호", example = "4321")
        String password,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 이메일", example = "jerry0339@naver.com")
        String email,
        @Schema(description = "회원 나이", example = "15")
        Integer age

) {
}
