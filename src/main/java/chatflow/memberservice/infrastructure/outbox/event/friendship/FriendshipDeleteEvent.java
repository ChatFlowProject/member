package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipDeleteEvent extends OutboxEvent {

    public FriendshipDeleteEvent(FriendshipEventPayload payload) {
        super("friendship", payload.id(), "friendshipDelete", payload, "");
    }

}
