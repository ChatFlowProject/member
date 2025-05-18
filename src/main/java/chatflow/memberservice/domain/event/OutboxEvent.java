package chatflow.memberservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class OutboxEvent {
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private Object payload;
    private String eventId;

    public void setEventId(String  eventId) {
        this.eventId = eventId;
    }

}
