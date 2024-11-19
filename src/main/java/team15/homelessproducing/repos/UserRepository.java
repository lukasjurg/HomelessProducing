package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
