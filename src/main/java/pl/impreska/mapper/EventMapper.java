package pl.impreska.mapper;

import pl.impreska.dto.EventResponseDto;
import pl.impreska.model.Event;
import pl.impreska.model.EventParticipant;
import pl.impreska.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static EventResponseDto toDto(Event event){
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer().getId());

        List<Long> participantsIds = event.getParticipants().stream()
                .map(EventParticipant::getUser)
                .map(User::getId)
                .toList();

        dto.setParticipants(participantsIds);
        return dto;
    }
}
