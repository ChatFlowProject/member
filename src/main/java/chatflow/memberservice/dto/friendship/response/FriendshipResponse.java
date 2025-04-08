package chatflow.memberservice.dto.friendship.response;

import chatflow.memberservice.entity.friendship.FriendRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record FriendshipResponse(
        @Schema(description = "친구 요청 상태", example = "INVALID_REQUEST / ALREADY_FRIENDS / REQUEST_SUCCESS / FRIENDSHIP_ESTABLISHED")
        FriendRequestStatus status
) {
}
