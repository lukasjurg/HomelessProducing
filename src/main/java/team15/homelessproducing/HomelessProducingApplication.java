package team15.homelessproducing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the HomelessRestApplication.
 * This class bootstraps and launches the Spring Boot application.
 */
@SpringBootApplication
public class HomelessProducingApplication {

    /**
     * The main method serves as the entry point for the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(HomelessProducingApplication.class, args);
    }
}
