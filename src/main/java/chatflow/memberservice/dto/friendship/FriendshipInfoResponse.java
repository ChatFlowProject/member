package chatflow.memberservice.dto.friendship;

import chatflow.memberservice.dto.member.response.MemberInfoResponse;

public record FriendshipInfoResponse(
        MemberInfoResponse from,
        MemberInfoResponse to
) {
}
