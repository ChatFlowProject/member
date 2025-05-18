package chatflow.memberservice.domain.repository;

import chatflow.memberservice.domain.model.outbox.EventStatus;
import chatflow.memberservice.domain.model.outbox.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByStatusIn(Collection<EventStatus> statuses);
    Optional<Outbox> findByEventId(String eventId);

}
