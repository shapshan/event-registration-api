package kz.iitu.registration.controller;

import kz.iitu.registration.model.EventInfo;
import kz.iitu.registration.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP layer only: reads the request, calls EventService, chooses the status code.
 * The list of events and the operations on it live in EventService.
 */
@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final EventService eventService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${app.course}")
    private String course;

    @Value("${app.semester}")
    private String semester;

    // Constructor-based dependency injection: Spring passes the EventService bean here.
    public RegistrationController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", eventService.getWelcomeMessage());
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("application", applicationName);
        info.put("course", course);
        info.put("semester", semester);
        return info;
    }

    // GET /api/events, GET /api/events?title=quiz, optional: ?title=web&location=room
    // @RequestParam reads the query string; required = false makes the filters optional.
    @GetMapping("/events")
    public List<EventInfo> getEvents(@RequestParam(required = false) String title,
                                     @RequestParam(required = false) String location) {
        return eventService.getEvents(title, location);
    }

    // GET /api/events/{id}: @PathVariable reads the id from the URL path.
    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id) {
        return eventService.getEventById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> notFound(id));
    }

    // POST /api/events: @RequestBody converts the JSON body to an EventInfo object.
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody EventInfo request) {
        if (isInvalid(request)) {
            return badRequest();
        }
        EventInfo created = eventService.createEvent(request.getTitle().trim(), request.getLocation().trim());
        return ResponseEntity.created(URI.create("/api/events/" + created.getId())).body(created);
    }

    // PUT /api/events/{id}: path variable + JSON body; replaces both fields, keeps the id.
    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id, @RequestBody EventInfo request) {
        if (isInvalid(request)) {
            return badRequest();
        }
        return eventService.updateEvent(id, request.getTitle().trim(), request.getLocation().trim())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> notFound(id));
    }

    // DELETE /api/events/{id}: 204 without a body, or 404 if the id does not exist.
    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.noContent().build();
        }
        return notFound(id);
    }

    // Simple input check: title and location must be present and not blank.
    private boolean isInvalid(EventInfo request) {
        return request == null
                || request.getTitle() == null || request.getTitle().isBlank()
                || request.getLocation() == null || request.getLocation().isBlank();
    }

    private ResponseEntity<?> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Title and location must not be empty"));
    }

    private ResponseEntity<?> notFound(long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Event with id " + id + " was not found"));
    }
}
