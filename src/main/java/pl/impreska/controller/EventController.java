package pl.impreska.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.impreska.dto.EventCreateRequest;
import pl.impreska.dto.EventResponseDto;
import pl.impreska.mapper.EventMapper;
import pl.impreska.model.Event;
import pl.impreska.model.User;
import pl.impreska.repository.EventRepository;
import pl.impreska.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
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
            eventService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
