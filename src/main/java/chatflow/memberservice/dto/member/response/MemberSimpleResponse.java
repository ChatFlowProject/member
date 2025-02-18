package chatflow.memberservice.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MemberSimpleResponse(
        @Schema(description = "회원 닉네임", example = "jerry0339")
        String nickname,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 가입 시기", example = "2008-04-07T15:00:00")
        LocalDateTime createdAt
) {
}
