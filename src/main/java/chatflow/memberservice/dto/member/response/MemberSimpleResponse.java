package chatflow.memberservice.dto.member.response;

import chatflow.memberservice.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberSimpleResponse(
        @Schema(description = "회원 고유키", example = "98bd5bf6-848a-43d4-8683-205523c9e359")
        UUID id,
        @Schema(description = "회원 닉네임", example = "jerry0339")
        String nickname,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 가입 시기", example = "2008-04-07T15:00:00")
        LocalDateTime createdAt
) {
    public static MemberSimpleResponse from(Member member) {
        return new MemberSimpleResponse(
                member.getId(),
                member.getNickname(),
                member.getName(),
                member.getCreatedAt()
        );
    }
}
