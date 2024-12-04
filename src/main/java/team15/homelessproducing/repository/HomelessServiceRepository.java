package team15.homelessproducing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team15.homelessproducing.model.HomelessService;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HomelessServiceRepository extends JpaRepository<HomelessService, Long> {

    @Query("SELECT h FROM HomelessService h WHERE h.startTime >= :start AND h.endTime <= :end")
    List<HomelessService> findByTimeRange(@Param("start") LocalTime start, @Param("end") LocalTime end);

    @Query("SELECT h FROM HomelessService h WHERE h.city.cityName = :cityName")
    List<HomelessService> findByCityName(@Param("cityName") String cityName);

    @Query("SELECT h FROM HomelessService h WHERE h.category.categoryName = :categoryName")
    List<HomelessService> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT h FROM HomelessService h WHERE " +
            "(:cityName IS NULL OR h.city.cityName = :cityName) AND " +
            "(:categoryName IS NULL OR h.category.categoryName = :categoryName) AND " +
            "(:startTime IS NULL OR h.startTime >= :startTime) AND " +
            "(:endTime IS NULL OR h.endTime <= :endTime)")
    List<HomelessService> filterByCityCategoryAndTime(
            @Param("cityName") String cityName,
            @Param("categoryName") String categoryName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);


}
