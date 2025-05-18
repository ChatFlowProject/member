package chatflow.memberservice.domain.event.member;

import chatflow.memberservice.domain.event.OutboxEvent;

public class MemberModifyStatusEvent extends OutboxEvent {

    public MemberModifyStatusEvent(String aggregateId, Object payload) {
        super("member", aggregateId, "memberModifyStatus", payload, "");
    }

}