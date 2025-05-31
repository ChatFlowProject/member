package chatflow.memberservice.infrastructure.outbox.scheduler;

import chatflow.memberservice.infrastructure.outbox.model.EventStatus;
import chatflow.memberservice.infrastructure.outbox.model.Outbox;
import chatflow.memberservice.infrastructure.repository.outbox.OutboxRepository;
import chatflow.memberservice.infrastructure.outbox.publisher.KafkaEventPublisher;
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

    // At-least-once 가정으로,
    // Consumer가 등록된 서비스에서 중복 처리를 방지해야 함
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
        // Scheduler에 의해 발생 가능한 중복 발행 및 처리에 대한 문제는 Inbox, Unique key, upsert, InternalKafkaListener 방식 등을 활용
    }

}
