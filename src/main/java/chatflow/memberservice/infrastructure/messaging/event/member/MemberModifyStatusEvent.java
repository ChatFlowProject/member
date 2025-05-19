package chatflow.memberservice.infrastructure.messaging.event.member;

import chatflow.memberservice.infrastructure.messaging.event.OutboxEvent;
import chatflow.memberservice.infrastructure.messaging.payload.MemberPayload;

public class MemberModifyStatusEvent extends OutboxEvent {

    public MemberModifyStatusEvent(String aggregateId, MemberPayload payload) {
        super("member", aggregateId, "memberModifyStatus", payload, "");
    }

}