package chatflow.memberservice.infrastructure.outbox.payload;

import chatflow.memberservice.domain.friendship.Friendship;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendshipEventPayload(
        String id,
        UUID fromMemberId,
        UUID toMemberId,
        LocalDateTime timestamp // 이벤트 발행 시간 - 오래된 이벤트 처리를 무시하기 위해 필요
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

        return new FriendshipEventPayload(
                String.format("%s:%s", fromId, toId),
                fromId,
                toId,
                LocalDateTime.now()
        );
    }
}
