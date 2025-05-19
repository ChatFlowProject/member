package chatflow.memberservice.infrastructure.outbox.event.member;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;

public class MemberDeleteEvent extends OutboxEvent {

    public MemberDeleteEvent(String aggregateId, MemberEventPayload payload) {
        super("member", aggregateId, "memberDelete", payload, "");
    }

}
