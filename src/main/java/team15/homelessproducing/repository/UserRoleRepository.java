package team15.homelessproducing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
