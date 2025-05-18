package chatflow.memberservice.domain.event.member;

import chatflow.memberservice.domain.event.OutboxEvent;

public class MemberUpdateEvent extends OutboxEvent {

    public MemberUpdateEvent(String aggregateId, Object payload) {
        super("member", aggregateId, "memberUpdate", payload, "");
    }

}