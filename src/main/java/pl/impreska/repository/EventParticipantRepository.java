package pl.impreska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.impreska.model.Event;
import pl.impreska.model.EventParticipant;

import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId);
}
