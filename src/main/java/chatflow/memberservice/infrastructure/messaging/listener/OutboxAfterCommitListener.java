package chatflow.memberservice.infrastructure.messaging.listener;

import chatflow.memberservice.domain.outbox.Outbox;
import chatflow.memberservice.exception.custom.EntityNotFoundException;
import chatflow.memberservice.infrastructure.messaging.event.OutboxEvent;
import chatflow.memberservice.infrastructure.messaging.publisher.KafkaEventPublisher;
import chatflow.memberservice.infrastructure.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OutboxAfterCommitListener {
    private final OutboxRepository outboxRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

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
