package chatflow.memberservice.infrastructure.messaging.event.member;

import chatflow.memberservice.infrastructure.messaging.event.OutboxEvent;
import chatflow.memberservice.infrastructure.messaging.payload.MemberPayload;

public class MemberUpdateEvent extends OutboxEvent {

    public MemberUpdateEvent(String aggregateId, MemberPayload payload) {
        super("member", aggregateId, "memberUpdate", payload, "");
    }

}