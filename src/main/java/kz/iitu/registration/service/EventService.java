package kz.iitu.registration.service;

import kz.iitu.registration.model.EventInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Business logic layer. Holds the in-memory list of events and all operations on it.
 * Data is lost when the application restarts (no database yet).
 */
@Service
public class EventService {

    private final List<EventInfo> events = new ArrayList<>();

    // Separate increasing counter: an id is never reused, even after a delete.
    // It starts above the largest sample id (3).
    private long nextId = 4;

    public EventService() {
        events.add(new EventInfo(1L, "Web Design Workshop", "Room 301"));
        events.add(new EventInfo(2L, "JavaScript Quiz", "Computer Lab 2"));
        events.add(new EventInfo(3L, "IT Career Talk", "Main Hall"));
    }

    public String getWelcomeMessage() {
        return "Welcome to the Event Registration API";
    }

    /**
     * Returns all events. Optional filters: the title (or location) must contain
     * the given text, ignoring case. A null or blank filter is ignored.
     */
    public synchronized List<EventInfo> getEvents(String title, String location) {
        List<EventInfo> result = new ArrayList<>();
        for (EventInfo event : events) {
            if (contains(event.getTitle(), title) && contains(event.getLocation(), location)) {
                result.add(event);
            }
        }
        return result;
    }

    public synchronized Optional<EventInfo> getEventById(long id) {
        return events.stream()
                .filter(event -> event.getId() == id)
                .findFirst();
    }

    /** Creates a new event with a server-assigned id. */
    public synchronized EventInfo createEvent(String title, String location) {
        EventInfo event = new EventInfo(nextId++, title, location);
        events.add(event);
        return event;
    }

    /** Replaces title and location of an existing event; the id stays the same. */
    public synchronized Optional<EventInfo> updateEvent(long id, String title, String location) {
        Optional<EventInfo> existing = getEventById(id);
        existing.ifPresent(event -> {
            event.setTitle(title);
            event.setLocation(location);
        });
        return existing;
    }

    /** Deletes an event. Returns false when the id does not exist. */
    public synchronized boolean deleteEvent(long id) {
        return events.removeIf(event -> event.getId() == id);
    }

    private boolean contains(String value, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT));
    }
}
