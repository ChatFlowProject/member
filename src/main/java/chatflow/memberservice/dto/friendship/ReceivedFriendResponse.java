package chatflow.memberservice.dto.friendship;

import chatflow.memberservice.dto.member.response.MemberSimpleResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReceivedFriendResponse(
        @Schema(description = "내가 받은 친구 요청 목록")
        List<MemberSimpleResponse> sentFriendRequests
) {
}
