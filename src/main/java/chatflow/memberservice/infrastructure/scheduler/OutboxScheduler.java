package chatflow.memberservice.infrastructure.scheduler;

import chatflow.memberservice.domain.model.outbox.EventStatus;
import chatflow.memberservice.domain.model.outbox.Outbox;
import chatflow.memberservice.infrastructure.repository.OutboxRepository;
import chatflow.memberservice.infrastructure.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Scheduled(fixedDelay = 600000) // 10분 간격
    @Transactional
    public void retryPendingMessages() {
        List<Outbox> eventList = outboxRepository.findByStatusIn(List.of(EventStatus.PENDING, EventStatus.FAILED));
        for (Outbox outbox : eventList) {
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

}
