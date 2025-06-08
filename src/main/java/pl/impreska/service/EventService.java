package pl.impreska.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.impreska.dto.EventCreateRequest;
import pl.impreska.dto.EventResponseDto;
import pl.impreska.dto.UpdateParticipantStatusRequest;
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

    public Optional<EventParticipant> updateParticipantStatus(Long eventId, Long userId, UpdateParticipantStatusRequest request){
        Optional<EventParticipant> participantOptional = participantRepository.findByEventIdAndUserId(eventId, userId);

        if(participantOptional.isPresent()){
            EventParticipant participant = participantOptional.get();

            if(request.getAttendanceStatus() != null){
                participant.setAttendanceStatus(request.getAttendanceStatus());
            }

            if(request.getDrinkingStatus() != null){
                participant.setDrinkingStatus(request.getDrinkingStatus());
            }

            if(request.getDrink() != null){
                participant.setDrink(request.getDrink());
            }

            EventParticipant save = participantRepository.save(participant);
            return Optional.of(save);
        }

        return Optional.empty();
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

        EventParticipant eventOrganizerParticipant = new EventParticipant();
        eventOrganizerParticipant.setEvent(savedEvent);
        eventOrganizerParticipant.setUser(organizer);
        eventOrganizerParticipant.setDrink("Nie wybrane");
        eventOrganizerParticipant.setAttendanceStatus(AttendanceStatus.NO_ANSWER);
        eventOrganizerParticipant.setDrinkingStatus(DrinkingStatus.NO_ANSWER);

        List<EventParticipant> participants = new ArrayList<>();

        participants.add(eventOrganizerParticipant);
        participantRepository.save(eventOrganizerParticipant);

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

    public boolean deleteEvent(Long id) {
        if(eventRepository.existsById(id)){
            eventRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}