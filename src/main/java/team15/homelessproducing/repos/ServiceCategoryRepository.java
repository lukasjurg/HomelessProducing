package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.ServiceCategory;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {
}
