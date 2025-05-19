package chatflow.memberservice.infrastructure.repository;

import chatflow.memberservice.domain.outbox.EventStatus;
import chatflow.memberservice.domain.outbox.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByStatusIn(Collection<EventStatus> statuses);
    Optional<Outbox> findByEventId(String eventId);

}
