package chatflow.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send")
    public String sendMessage(
        @RequestParam String topic,
        @RequestParam String message
    ) {
        kafkaTemplate.send(topic, message);
        return "Message sent: " + message;
    }
}