//package pl.impreska;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import pl.impreska.model.*;
//import pl.impreska.repository.EventParticipantRepository;
//import pl.impreska.repository.EventRepository;
//import pl.impreska.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class TestDataLoader implements CommandLineRunner {
//
//    private final UserRepository userRepo;
//    private final EventRepository eventRepo;
//    private final EventParticipantRepository participantRepo;
//
//    public TestDataLoader(UserRepository userRepo, EventRepository eventRepo, EventParticipantRepository participantRepo) {
//        this.userRepo = userRepo;
//        this.eventRepo = eventRepo;
//        this.participantRepo = participantRepo;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (userRepo.count() == 0 && eventRepo.count() == 0 && participantRepo.count() == 0) {
//
//            // Tworzymy użytkowników
//            List<User> users = new ArrayList<>();
//            for (int i = 1; i <= 5; i++) {
//                User user = new User();
//                user.setUsername("user" + i);
//                user.setPassword("pass" + i);
//                users.add(userRepo.save(user));
//            }
//
//            // Tworzymy wydarzenia
//            List<Event> events = new ArrayList<>();
//            for (int i = 1; i <= 3; i++) {
//                Event event = new Event();
//                event.setName("Wydarzenie " + i);
//                event.setDescription("Opis wydarzenia " + i);
//                event.setLocation("Miasto " + i);
//                event.setStartTime(LocalDateTime.now().plusDays(i));
//                event.setDuration(3 + i);
//                event.setOrganizer(users.get(i % users.size()));
//                events.add(eventRepo.save(event));
//            }
//
//            // Statusy do losowania
//            AttendanceStatus[] attendanceStatuses = AttendanceStatus.values();
//            DrinkingStatus[] drinkingStatuses = DrinkingStatus.values();
//            String[] drinks = {"Piwo", "Wódka", "Wino", "Sok", "Woda"};
//
//            // Dodajemy uczestników
//            Random rand = new Random();
//            for (Event event : events) {
//                // Każde wydarzenie będzie miało 3–5 uczestników
//                Collections.shuffle(users);
//                int participantCount = 3 + rand.nextInt(3); // 3-5 osób
//                for (int i = 0; i < participantCount; i++) {
//                    User user = users.get(i);
//                    EventParticipant participant = new EventParticipant();
//                    participant.setUser(user);
//                    participant.setEvent(event);
//                    participant.setAttendanceStatus(attendanceStatuses[rand.nextInt(attendanceStatuses.length)]);
//                    participant.setDrinkingStatus(drinkingStatuses[rand.nextInt(drinkingStatuses.length)]);
//                    participant.setDrink(drinks[rand.nextInt(drinks.length)]);
//                    participantRepo.save(participant);
//                }
//            }
//        }
//    }
//}