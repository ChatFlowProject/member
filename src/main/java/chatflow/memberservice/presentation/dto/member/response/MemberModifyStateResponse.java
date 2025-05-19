package chatflow.memberservice.presentation.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberModifyStateResponse(
        @Schema(description = "회원 접속 상태", example = "OFFLINE")
        String memberState
) {
}
