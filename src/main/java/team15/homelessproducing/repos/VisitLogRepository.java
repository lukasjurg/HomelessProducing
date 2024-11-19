package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import team15.homelessproducing.model.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Integer> {
}
