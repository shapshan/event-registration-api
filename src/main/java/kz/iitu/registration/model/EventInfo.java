package kz.iitu.registration.model;

/**
 * Event model. A regular Java class with a no-argument constructor and setters,
 * so Jackson can create it from the JSON request body: {"title":"...","location":"..."}.
 * The id is assigned by the server only; a client-supplied id is ignored.
 */
public class EventInfo {

    private Long id;
    private String title;
    private String location;

    public EventInfo() {
    }

    public EventInfo(Long id, String title, String location) {
        this.id = id;
        this.title = title;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
