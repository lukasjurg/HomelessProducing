package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team15.homelessproducing.model.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.role_name = :roleName")
    Optional<UserRole> findByRoleName(@Param("roleName") String roleName);
}
