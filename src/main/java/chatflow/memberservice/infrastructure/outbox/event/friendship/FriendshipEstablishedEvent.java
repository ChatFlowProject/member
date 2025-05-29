package chatflow.memberservice.infrastructure.outbox.event.friendship;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.FriendshipEventPayload;

public class FriendshipEstablishedEvent extends OutboxEvent {

    public FriendshipEstablishedEvent(String aggregateId, FriendshipEventPayload payload) {
        super("friendship", aggregateId, "friendshipEstablished", payload, "");
    }

}
