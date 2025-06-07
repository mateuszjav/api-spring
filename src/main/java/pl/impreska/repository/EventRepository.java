package pl.impreska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.impreska.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
