package chatflow.memberservice.presentation.dto.sign_up;

import chatflow.memberservice.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "98bd5bf6-848a-43d4-8683-205523c9e359")
        UUID id,
        @Schema(description = "회원 닉네임", example = "jerry0339")
        String nickname,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 생년월일", example = "2000-04-07")
        String birth
) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getNickname(),
                member.getName(),
                member.getBirth().toString()
        );
    }
}
