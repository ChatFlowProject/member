package chatflow.memberservice.controller.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MemberListRequest(
        @NotNull(message = "조회할 회원의 ID 목록을 입력해 주세요")
        @Schema(description = "회원 ID 목록", example = "[\"f3ca6705-9cde-4d6c-a3bf-eb89703ac1d7\", " +
                "\"b53a5d14-5581-497c-8b3e-27de52a9640c\", \"107222e0-a6e3-4838-93a8-ede66ab152d0\"]")
        List<UUID> memberIds
) {
}
