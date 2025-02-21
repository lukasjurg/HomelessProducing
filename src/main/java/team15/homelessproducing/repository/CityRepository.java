package team15.homelessproducing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team15.homelessproducing.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
