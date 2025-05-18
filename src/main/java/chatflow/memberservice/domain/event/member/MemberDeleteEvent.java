package chatflow.memberservice.domain.event.member;

import chatflow.memberservice.domain.event.OutboxEvent;

public class MemberDeleteEvent extends OutboxEvent {

    public MemberDeleteEvent(String aggregateId, Object payload) {
        super("member", aggregateId, "memberDelete", payload, "");
    }

}
