package chatflow.memberservice.infrastructure.kafka;

import chatflow.memberservice.domain.model.outbox.EventStatus;
import chatflow.memberservice.infrastructure.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternalKafkaListener {
    private final OutboxRepository outboxRepository;

    // At-least-once 전송을 가정한 중복 레코드 발행 최소화 (완전 방지는 불가능)
    @KafkaListener(topics = "member", groupId = "member-consumer-group")
    public void consumeMemberEvents(String message, @Header("eventId") String eventId) {
        try {
            // eventId 기준으로 Outbox 조회 후 상태 업데이트
            outboxRepository.findByEventId(eventId).ifPresent(outbox -> {
                if (outbox.getStatus() != EventStatus.SUCCESS) {
                    outbox.markSuccess();
                    outboxRepository.save(outbox);
                }
            });

        } catch (Exception e) {
            // At-least-once 가정으로, Consumer에서 중복 처리를 방지해야 함 (Unique 키나 upsert 방식 사용하면 됨)
            log.warn("Internal listener failed for eventId {}: {}", eventId, e.getMessage());
        }
    }

}
