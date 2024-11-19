package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.ServiceAvailability;

public interface ServiceAvailabilityRepository extends JpaRepository<ServiceAvailability, Integer> {
}
