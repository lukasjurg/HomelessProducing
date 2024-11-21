package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team15.homelessproducing.model.VisitLog;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Integer> {
    void deleteByService_ServiceId(Integer serviceId); // Matches `VisitLog.service`
}
