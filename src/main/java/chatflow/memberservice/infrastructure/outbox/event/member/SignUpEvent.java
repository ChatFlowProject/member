package chatflow.memberservice.infrastructure.outbox.event.member;

import chatflow.memberservice.infrastructure.outbox.event.OutboxEvent;
import chatflow.memberservice.infrastructure.outbox.payload.MemberEventPayload;

public class SignUpEvent extends OutboxEvent {

    public SignUpEvent(String aggregateId, MemberEventPayload payload) {
        super("member", aggregateId, "signUp", payload, "");
    }

}