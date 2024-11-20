package team15.homelessproducing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repos.UserRoleRepository;

import java.util.Optional;

@SpringBootApplication
public class HomelessProducingApplication implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public static void main(String[] args) {
        SpringApplication.run(HomelessProducingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<UserRole> userRole = userRoleRepository.findByRoleName("User");
        if (userRole.isPresent()) {
            System.out.println("User role found: " + userRole.get().getRole_name());
        } else {
            System.out.println("User role not found!");
        }
    }
}
