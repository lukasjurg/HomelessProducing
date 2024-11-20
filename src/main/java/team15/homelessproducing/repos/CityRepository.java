package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team15.homelessproducing.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
