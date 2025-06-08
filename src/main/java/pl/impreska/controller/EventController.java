package pl.impreska.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.impreska.dto.EventCreateRequest;
import pl.impreska.dto.EventResponseDto;
import pl.impreska.dto.UpdateParticipantStatusRequest;
import pl.impreska.exception.UserNotFoundException;
import pl.impreska.mapper.EventMapper;
import pl.impreska.model.Event;
import pl.impreska.model.User;
import pl.impreska.repository.EventRepository;
import pl.impreska.repository.UserRepository;
import pl.impreska.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id){
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateRequest request){
        Event savedEvent = eventService.createEventWithParticipants(request);
        EventResponseDto response = EventMapper.toDto(savedEvent);
        return ResponseEntity.status(201).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        if(eventService.existsById(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/participants/me")
    public ResponseEntity<?> updateParticipantStatus(
            @PathVariable Long eventId,
            @RequestBody UpdateParticipantStatusRequest request,
            Authentication authentication) {
        String name = authentication.getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new IllegalArgumentException("Nie można znaleźć zalogowanego użytkowniuka"));

        return eventService.updateParticipantStatus(eventId, user.getId(), request)
                .map(updated -> ResponseEntity.ok().body(updated))
                .orElse(ResponseEntity.notFound().build());

    }

}
