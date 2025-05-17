package chatflow.memberservice.controller.dto.sign_up;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", message = "잘못된 이메일 형식입니다.")
        @Schema(description = "회원 이메일", example = "jerry0339@naver.com")
        String email,
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Schema(description = "회원 비밀번호", example = "4321")
        String password,
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "20자 이내로 입력해 주세요.")
        @Schema(description = "회원 닉네임", example = "jerry0339")
        String nickname,
        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "20자 이내로 입력해 주세요.")
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @NotBlank(message = "생년월일을 입력해 주세요.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 `yyyy-MM-dd` 형식으로 입력 되어야 합니다.")
        @Schema(description = "회원 생년월일", example = "2000-04-07")
        String birth
) {
}
