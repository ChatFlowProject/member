package chatflow.memberservice.infrastructure.outbox.event.member;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;

public class MemberModifyStatusEvent extends OutboxEvent {

    public MemberModifyStatusEvent(String aggregateId, MemberEventPayload payload) {
        super("member", aggregateId, "memberModifyStatus", payload, "");
    }

}