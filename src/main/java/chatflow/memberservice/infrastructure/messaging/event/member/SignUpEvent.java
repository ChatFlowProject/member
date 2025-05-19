package chatflow.memberservice.infrastructure.messaging.event.member;

import chatflow.memberservice.infrastructure.messaging.event.OutboxEvent;
import chatflow.memberservice.infrastructure.messaging.payload.MemberPayload;

public class SignUpEvent extends OutboxEvent {

    public SignUpEvent(String aggregateId, MemberPayload payload) {
        super("member", aggregateId, "signUp", payload, "");
    }

}