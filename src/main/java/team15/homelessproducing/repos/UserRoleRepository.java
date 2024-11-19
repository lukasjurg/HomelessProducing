package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessapp.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}
