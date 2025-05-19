package chatflow.memberservice.infrastructure.outbox.publisher;

import chatflow.memberservice.common.exception.custom.KafkaEventSendException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String topic, String eventId, String eventType, String key, String payload) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);
            record.headers().add("eventId", eventId.getBytes(StandardCharsets.UTF_8));
            record.headers().add("eventType", eventType.getBytes(StandardCharsets.UTF_8));

            // .get() -> Kafka 메시지 전송 결과를 동기적으로 기다리기 위함 (Kafka 전송 성공 여부가 필요하기 때문)
            kafkaTemplate.send(record).get();
        } catch (Exception e) {
            throw new KafkaEventSendException("Kafka send failed: " + e.getMessage());
        }
    }

}
