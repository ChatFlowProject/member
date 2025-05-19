package chatflow.memberservice.presentation.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MemberModifyStateRequest(
        @NotNull(message = "변경할 회원 상태를 입력해 주세요.")
        @Schema(description = "회원 접속 상태", example = "ONLINE/IDLE/DO_NOT_DISTURB/OFFLINE/ or 온라인/자리비움/방해금지/오프라인")
        String memberState
) {
}
