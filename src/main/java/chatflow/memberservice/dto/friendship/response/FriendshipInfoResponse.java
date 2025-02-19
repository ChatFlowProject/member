package chatflow.memberservice.dto.friendship.response;

import chatflow.memberservice.dto.member.response.MemberInfoResponse;

public record FriendshipInfoResponse(
        MemberInfoResponse from,
        MemberInfoResponse to
) {
}
