package team15.homelessproducing.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team15.homelessproducing.model.AppService;

import java.time.LocalTime;
import java.util.List;

public interface AppServiceRepository extends JpaRepository<AppService, Integer> {

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findAvailableServicesWithinHours(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.category.categoryName = :categoryName")
    List<AppService> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.category.categoryName = :categoryName AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findByCategoryAndAvailability(
            @Param("categoryName") String categoryName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.city.cityName = :cityName")
    List<AppService> findByCityName(@Param("cityName") String cityName);

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.city.cityName = :cityName AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findByCityAndAvailability(
            @Param("cityName") String cityName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.category.categoryName = :categoryName AND s.city.cityName = :cityName")
    List<AppService> findByCategoryAndCity(
            @Param("categoryName") String categoryName,
            @Param("cityName") String cityName
    );

    @Query("SELECT s FROM AppService s JOIN FETCH s.city JOIN FETCH s.category WHERE s.category.categoryName = :categoryName AND s.city.cityName = :cityName AND s.startTime <= :endTime AND s.endTime >= :startTime")
    List<AppService> findByCategoryCityAndAvailability(
            @Param("categoryName") String categoryName,
            @Param("cityName") String cityName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
