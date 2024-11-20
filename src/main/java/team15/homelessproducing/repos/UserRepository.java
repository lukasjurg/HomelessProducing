package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
