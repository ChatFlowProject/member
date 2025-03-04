package chatflow.memberservice.dto.sign_in;

import chatflow.memberservice.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignInResponse(
        @Schema(description = "회원 고유키", example = "98bd5bf6-848a-43d4-8683-205523c9e359")
        UUID id,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 유형", example = "MEMBER")
        MemberType type,
        String token
) {
}
