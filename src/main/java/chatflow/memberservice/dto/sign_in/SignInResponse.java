package chatflow.memberservice.dto.sign_in;

import chatflow.memberservice.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignInResponse(
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 유형", example = "MEMBER")
        MemberType type,
        String token
) {
}
