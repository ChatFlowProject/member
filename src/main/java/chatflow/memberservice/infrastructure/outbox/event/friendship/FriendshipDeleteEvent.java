package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipDeleteEvent extends OutboxEvent {

    public FriendshipDeleteEvent(String aggregateId, FriendshipEventPayload payload) {
        super("friendship", aggregateId, "friendshipDelete", payload, "");
    }

}
