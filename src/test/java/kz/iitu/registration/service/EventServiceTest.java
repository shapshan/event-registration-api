package kz.iitu.registration.service;

import kz.iitu.registration.model.EventInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    private final EventService service = new EventService();

    @Test
    void startsWithThreeEvents() {
        assertEquals(3, service.getEvents(null, null).size());
    }

    @Test
    void searchByTitleIgnoresCase() {
        assertEquals(1, service.getEvents("QUIZ", null).size());
        assertTrue(service.getEvents("nothing", null).isEmpty());
    }

    @Test
    void searchCombinesTitleAndLocation() {
        assertEquals(1, service.getEvents("web", "room").size());
        assertTrue(service.getEvents("web", "hall").isEmpty());
    }

    @Test
    void createAssignsNewId() {
        EventInfo created = service.createEvent("Hackathon", "Lab 5");
        assertEquals(4L, created.getId());
        assertEquals(4, service.getEvents(null, null).size());
    }

    @Test
    void idIsNotReusedAfterDelete() {
        EventInfo first = service.createEvent("A", "B");
        assertTrue(service.deleteEvent(first.getId()));
        EventInfo second = service.createEvent("C", "D");
        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void updateReplacesFieldsAndKeepsId() {
        EventInfo updated = service.updateEvent(1, "New title", "New place").orElseThrow();
        assertEquals(1L, updated.getId());
        assertEquals("New title", service.getEventById(1).orElseThrow().getTitle());
    }

    @Test
    void missingIdGivesEmptyOrFalse() {
        assertTrue(service.getEventById(999).isEmpty());
        assertTrue(service.updateEvent(999, "x", "y").isEmpty());
        assertFalse(service.deleteEvent(999));
    }
}
