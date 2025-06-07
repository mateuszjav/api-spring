package pl.impreska.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.impreska.dto.EventCreateRequest;
import pl.impreska.dto.EventResponseDto;
import pl.impreska.mapper.EventMapper;
import pl.impreska.model.*;
import pl.impreska.repository.EventParticipantRepository;
import pl.impreska.repository.EventRepository;
import pl.impreska.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventParticipantRepository participantRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, EventParticipantRepository participantRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public boolean existsById(Long id){
        return eventRepository.existsById(id);
    }

    public void deleteById(Long id){
        eventRepository.deleteById(id);
    }


    public Event createEventWithParticipants(EventCreateRequest request) {
        User organizer = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono organizatora o takim id"));

        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setDuration(request.getDuration());
        event.setStartTime(request.getStartTime());
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);

        List<EventParticipant> participants = new ArrayList<>();

        if(request.getParticipants() != null) {
            for (Long userId : request.getParticipants()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono goscia o id: " + userId));

                EventParticipant eventParticipant = new EventParticipant();

                eventParticipant.setEvent(savedEvent);
                eventParticipant.setUser(user);
                eventParticipant.setDrink("Nie wybrane");
                eventParticipant.setAttendanceStatus(AttendanceStatus.NO_ANSWER);
                eventParticipant.setDrinkingStatus(DrinkingStatus.NO_ANSWER);

                participants.add(eventParticipant);

                participantRepository.save(eventParticipant);
            }
        }
        savedEvent.setParticipants(participants);

        return savedEvent;

    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }


}