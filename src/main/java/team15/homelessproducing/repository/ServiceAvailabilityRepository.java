package team15.homelessproducing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team15.homelessproducing.model.ServiceAvailability;

@Repository
public interface ServiceAvailabilityRepository extends JpaRepository<ServiceAvailability, Long> {
}
