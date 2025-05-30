package chatflow.memberservice.infrastructure.outbox.payload;

import chatflow.memberservice.domain.friendship.Friendship;

import java.util.UUID;

public record FriendshipEventPayload(
        UUID id,
        UUID fromMemberId,
        UUID toMemberId

) {
    public static FriendshipEventPayload from(Friendship friendship) {
        UUID fromId = friendship.getFromMember().getId();
        UUID toId = friendship.getToMember().getId();
        // 큰 UUID를 fromID로
        if (fromId.compareTo(toId) < 0) {
            UUID temp = fromId;
            fromId = toId;
            toId = temp;
        }

        return new FriendshipEventPayload(fromId, fromId, toId);
    }
}
