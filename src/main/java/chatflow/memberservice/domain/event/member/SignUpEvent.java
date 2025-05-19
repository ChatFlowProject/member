package chatflow.memberservice.domain.event.member;

import chatflow.memberservice.domain.event.OutboxEvent;

public class SignUpEvent extends OutboxEvent {

    public SignUpEvent(String aggregateId, Object payload) {
        super("member", aggregateId, "signUp", payload, "");
    }

}