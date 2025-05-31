package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipEstablishedEvent extends OutboxEvent {

    public FriendshipEstablishedEvent(FriendshipEventPayload payload) {
        super("friendship", payload.id(), "friendshipEstablished", payload, "");
    }

}
