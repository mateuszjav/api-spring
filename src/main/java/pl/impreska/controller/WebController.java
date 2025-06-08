package pl.impreska.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.impreska.service.EventService;

@Controller
public class WebController {
    private final EventService eventService;

    public WebController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String showAllEvents(Model model){
        model.addAttribute("events", eventService.getAllEvents());
        return "events-list";
    }
}
