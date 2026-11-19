**Event Registration API (Laboratory Work 7: REST controller, CRUD)**

Spring Boot backend that creates, reads, updates, deletes and searches events with HTTP requests.
Data is stored in memory only: all changes disappear when the application restarts.

Author: Shapshan Abylaikhan, group Ds2504

**Requirements**
- JDK 17 or later
- Gradle wrapper (no separate Gradle installation needed)
- Postman, Insomnia, IntelliJ HTTP Client or curl for POST, PUT and DELETE (a browser is enough only for GET)

**How to run**

Linux / macOS: `./gradlew bootRun`

Windows: `gradlew.bat bootRun`

The application listens on http://localhost:8080. Unit tests: `./gradlew test`.

**Endpoints**

| Request | Description | Success | Errors |
| --- | --- | --- | --- |
| GET /api/events | All events. Optional filters: ?title=quiz and ?location=room (ignore case, can be combined) | 200, JSON array (empty array if nothing matches) | none |
| GET /api/events/{id} | One event by id | 200 | 404 if the id does not exist |
| POST /api/events | Create an event from a JSON body; the server assigns the id | 201 with the created event | 400 if title or location is missing or blank |
| PUT /api/events/{id} | Replace title and location, the id is preserved | 200 with the updated event | 404 if the id does not exist, 400 for invalid body |
| DELETE /api/events/{id} | Delete an event | 204 without a body | 404 if the id does not exist |
| GET /api/hello, GET /api/info | Lab 6 endpoints (welcome message, settings) | 200 | none |

Ids are assigned by the server with an increasing counter that starts at 4 and is never reused, even after a delete.

**Example JSON body (POST and PUT)**
```json
{
  "title": "Web Design Workshop",
  "location": "Room 101"
}
```

**Example requests with curl**
```bash
curl http://localhost:8080/api/events
curl "http://localhost:8080/api/events?title=quiz"
curl http://localhost:8080/api/events/1
curl -i -X POST http://localhost:8080/api/events -H "Content-Type: application/json" -d '{"title":"Hackathon","location":"Lab 5"}'
curl -i -X PUT http://localhost:8080/api/events/4 -H "Content-Type: application/json" -d '{"title":"Hackathon 2","location":"Main Hall"}'
curl -i -X DELETE http://localhost:8080/api/events/4
```
On Windows PowerShell use `curl.exe` and escape the quotes inside the JSON, or use Postman.

**Structure**
```
src/main/java/kz/iitu/registration/
  EventRegistrationApplication.java
  controller/RegistrationController.java   HTTP layer: mappings, status codes
  service/EventService.java                in-memory list, id counter, CRUD and search
  model/EventInfo.java                     id, title, location
src/main/resources/application.properties
```
The controller receives EventService through its constructor and only handles HTTP;
all work with the list is done in the service.
