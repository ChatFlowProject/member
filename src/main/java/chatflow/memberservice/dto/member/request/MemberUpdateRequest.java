package chatflow.memberservice.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @NotBlank(message = "기존의 비밀번호를 입력해 주세요.")
        @Schema(description = "회원 비밀번호", example = "4321")
        String password,
        @NotBlank(message = "새로운 비밀번호를 입력해 주세요.")
        @Schema(description = "회원 새 비밀번호", example = "1234")
        String newPassword,
        @NotBlank(message = "변경할 이름을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "20자 이내로 입력해 주세요.")
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @NotBlank(message = "변경할 생년월일을 입력해 주세요.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 `yyyy-MM-dd` 형식으로 입력 되어야 합니다.")
        @Schema(description = "회원 생년월일", example = "2000-04-07")
        String birth,
        @Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$", message = "올바른 URL 형식이어야 합니다.")
        @Schema(description = "아바타 이미지 url", example = "https://snowball-bucket.s3.ap-northeast-2.amazonaws.com/f41b6bb9-3jerry.png")
        String avatarUrl
) {
}
