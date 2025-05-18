package chatflow.memberservice.infrastructure.event;

import chatflow.memberservice.domain.event.OutboxEvent;
import chatflow.memberservice.domain.model.outbox.EventStatus;
import chatflow.memberservice.domain.model.outbox.Outbox;
import chatflow.memberservice.infrastructure.repository.OutboxRepository;
import chatflow.memberservice.exception.custom.EntityNotFoundException;
import chatflow.memberservice.exception.custom.InternalServiceException;
import chatflow.memberservice.infrastructure.kafka.KafkaEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxEventListener {
    private final OutboxRepository outboxRepository;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEventBeforeCommit(OutboxEvent event) {
        try {
            String eventId = UUID.randomUUID().toString();
            String jsonPayload = objectMapper.writeValueAsString(event.getPayload());
            Outbox outbox = Outbox.builder()
                    .eventId(eventId)
                    .aggregateType(event.getAggregateType())
                    .aggregateId(event.getAggregateId())
                    .eventType(event.getEventType())
                    .payload(jsonPayload)
                    .status(EventStatus.PENDING)
                    .build();
            outboxRepository.save(outbox);
            event.setEventId(eventId); // Outbox의 eventId를 이벤트에 설정 (AFTER_COMMIT 단계에서 사용)
        } catch (JsonProcessingException e) {
            throw new InternalServiceException("Payload serialization failed: " + e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOutboxEventAfterCommit(OutboxEvent event) {
        String eventId = event.getEventId();
        Outbox outbox = outboxRepository.findByEventId(eventId)
                .orElseThrow(() -> new EntityNotFoundException("eventId에 대한 OutBox 데이터가 존재하지 않습니다."));

        try {
            kafkaEventPublisher.sendEvent(
                    outbox.getAggregateType(),   // topic
                    outbox.getEventId(),         // eventId (header & Outbox Unique key)
                    outbox.getEventType(),       // eventType (header)
                    outbox.getAggregateId(),     // record key
                    outbox.getPayload()          // record message
            );
            outbox.markSuccess();
        } catch (Exception e) {
            outbox.markFailed();
        }
        outboxRepository.save(outbox);
    }

}
