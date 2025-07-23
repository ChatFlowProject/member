package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipRequestEvent extends OutboxEvent {

    public FriendshipRequestEvent(FriendshipEventPayload payload) {
        super("friendship", payload.id(), "friendshipRequest", payload, "");
    }

}
