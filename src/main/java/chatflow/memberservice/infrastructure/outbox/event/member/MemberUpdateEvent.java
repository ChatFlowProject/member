package chatflow.memberservice.infrastructure.outbox.event.member;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;

public class MemberUpdateEvent extends OutboxEvent {

    public MemberUpdateEvent(String aggregateId, MemberEventPayload payload) {
        super("member", aggregateId, "memberUpdate", payload, "");
    }

}