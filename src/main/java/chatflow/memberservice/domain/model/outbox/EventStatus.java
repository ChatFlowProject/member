package chatflow.memberservice.domain.model.outbox;

import lombok.Getter;

@Getter
public enum EventStatus {
    PENDING, SUCCESS, FAILED
}
