package chatflow.memberservice.infrastructure.messaging.event.member;

import chatflow.memberservice.infrastructure.messaging.event.OutboxEvent;
import chatflow.memberservice.infrastructure.messaging.payload.MemberPayload;

public class MemberDeleteEvent extends OutboxEvent {

    public MemberDeleteEvent(String aggregateId, MemberPayload payload) {
        super("member", aggregateId, "memberDelete", payload, "");
    }

}
