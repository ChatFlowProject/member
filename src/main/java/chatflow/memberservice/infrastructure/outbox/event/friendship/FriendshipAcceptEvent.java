package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipAcceptEvent extends OutboxEvent {

    public FriendshipAcceptEvent(String aggregateId, FriendshipEventPayload payload) {
        super("friendship", aggregateId, "friendshipAccept", payload, "");
    }

}
