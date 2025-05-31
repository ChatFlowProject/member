package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipAcceptEvent extends OutboxEvent {

    public FriendshipAcceptEvent(FriendshipEventPayload payload) {
        super("friendship", payload.id(), "friendshipAccept", payload, "");
    }

}
