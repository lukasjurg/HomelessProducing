package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team15.homelessproducing.model.AppService;

import java.time.LocalTime;
import java.util.List;

public interface AppServiceRepository extends JpaRepository<AppService, Integer> {

    /**
     * Find all services available within a specific time range.
     * Services are considered available if their start time is before or at the end time
     * and their end time is after or at the start time.
     *
     * @param startTime The start of the desired time range.
     * @param endTime   The end of the desired time range.
     * @return A list of services available within the time range.
     */
    @Query("SELECT s FROM AppService s WHERE s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findAvailableServicesWithinHours(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    /**
     * Find all services belonging to a specific category by category name.
     *
     * @param categoryName The name of the category to filter by.
     * @return A list of services in the specified category.
     */
    @Query("SELECT s FROM AppService s WHERE s.category.categoryName = :categoryName")
    List<AppService> findByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Find all services belonging to a specific category and available within a specific time range.
     *
     * @param categoryName The name of the category to filter by.
     * @param startTime    The start of the desired time range.
     * @param endTime      The end of the desired time range.
     * @return A list of services in the specified category and time range.
     */
    @Query("SELECT s FROM AppService s WHERE s.category.categoryName = :categoryName AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findByCategoryAndAvailability(
            @Param("categoryName") String categoryName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
