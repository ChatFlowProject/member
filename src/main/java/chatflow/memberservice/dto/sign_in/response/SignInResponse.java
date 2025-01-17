package chatflow.memberservice.dto.sign_in.response;

import chatflow.memberservice.common.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignInResponse(
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 유형", example = "USER")
        MemberType type,
        String token
) {
}
