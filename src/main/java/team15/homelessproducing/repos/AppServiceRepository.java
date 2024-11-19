package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team15.homelessapp.model.AppService;

import java.time.LocalTime;
import java.util.List;

public interface AppServiceRepository extends JpaRepository<AppService, Integer> {

    @Query("SELECT s FROM AppService s WHERE s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findAvailableServicesWithinHours(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT s FROM AppService s WHERE s.category.category_name = :categoryName")
    List<AppService> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT s FROM AppService s WHERE s.category.category_name = :categoryName AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findByCategoryAndAvailability(
            @Param("categoryName") String categoryName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
