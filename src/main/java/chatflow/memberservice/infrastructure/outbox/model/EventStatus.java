package chatflow.memberservice.infrastructure.outbox.model;

import lombok.Getter;

@Getter
public enum EventStatus {
    PENDING, // 이벤트 발행 대기중 - beforeCommit 단계 까지만 동작한 경우
    SUCCESS, // 발행 성공 - 정상 작동한 경우
    FAILED   // 발행 실패 - afterCommit 단계에서 이벤트 발행 도중 문제가 생긴 경우
}
