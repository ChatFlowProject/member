package chatflow.memberservice.infrastructure.outbox.listener;

import chatflow.memberservice.infrastructure.outbox.model.EventStatus;
import chatflow.memberservice.infrastructure.repository.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberInternalKafkaListener {
    private final OutboxRepository outboxRepository;

    // At-least-once 전송을 가정한 중복 발행 방지 및 재처리 방지
    // 엄격한 중복 발행 방지 및 재처리 방지가 필요한 경우 InternalKafkaListener 사용하면 좋을듯
    @KafkaListener(topics = "member", groupId = "member-internal-consumer-group") // topic별로 정의해야 함
    public void consumeMemberEvents(String message, @Header("eventId") String eventId) {
        // eventId 기준으로 Outbox 조회 후 상태 업데이트
        outboxRepository.findByEventId(eventId).ifPresent(outbox -> {
            if (outbox.getStatus() != EventStatus.SUCCESS) {
                outbox.markSuccess();
                outboxRepository.save(outbox);
            }
        });
    }

}
