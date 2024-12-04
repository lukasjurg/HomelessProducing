package team15.homelessproducing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
