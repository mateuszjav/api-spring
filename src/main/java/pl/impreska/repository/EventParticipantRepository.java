package pl.impreska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.impreska.model.EventParticipant;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
}
