package chatflow.memberservice.infrastructure.outbox.payload;

import chatflow.memberservice.domain.friendship.Friendship;

import java.util.UUID;

public record FriendshipEventPayload(
        Long id,
        UUID fromMemberId,
        UUID toMemberId

) {
    public static FriendshipEventPayload from(Friendship friendship) {
        return new FriendshipEventPayload(
                friendship.getId(),
                friendship.getFromMember().getId(),
                friendship.getToMember().getId()
        );
    }
}
