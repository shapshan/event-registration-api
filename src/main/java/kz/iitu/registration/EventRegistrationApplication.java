package kz.iitu.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application.
 * @SpringBootApplication enables component scanning (controller, service),
 * auto-configuration (embedded Tomcat, JSON) and Java-based configuration.
 */
@SpringBootApplication
public class EventRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventRegistrationApplication.class, args);
    }
}
