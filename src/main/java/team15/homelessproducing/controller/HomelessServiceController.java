package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.HomelessService;
import team15.homelessproducing.service.HomelessServiceService;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/homeless-services")
public class HomelessServiceController {

    @Autowired
    private HomelessServiceService homelessServiceService;

    // Get all homeless services
    @GetMapping
    public List<HomelessService> getAllHomelessServices() {
        return homelessServiceService.getAllHomelessServices();
    }

    // Get a single homeless service by ID
    @GetMapping("/{id}")
    public HomelessService getHomelessServiceById(@PathVariable Long id) {
        return homelessServiceService.getHomelessServiceById(id);
    }

    // Create a new homeless service
    @PostMapping
    public HomelessService createHomelessService(@RequestBody HomelessService homelessService) {
        return homelessServiceService.createHomelessService(homelessService);
    }

    // Update an existing homeless service
    @PutMapping("/{id}")
    public HomelessService updateHomelessService(@PathVariable Long id, @RequestBody HomelessService homelessServiceDetails) {
        return homelessServiceService.updateHomelessService(id, homelessServiceDetails);
    }

    // Delete a homeless service by ID
    @DeleteMapping("/{id}")
    public void deleteHomelessService(@PathVariable Long id) {
        homelessServiceService.deleteHomelessService(id);
    }

    // Filter services by a specific time range
    @GetMapping("/filter-by-time")
    public List<HomelessService> filterByTimeRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        return homelessServiceService.filterByTimeRange(startTime, endTime);
    }

    // Filter services by city name
    @GetMapping("/by-city")
    public List<HomelessService> filterByCityName(@RequestParam String cityName) {
        return homelessServiceService.filterByCityName(cityName);
    }

    // Filter services by category name
    @GetMapping("/by-category")
    public List<HomelessService> filterByCategoryName(@RequestParam String categoryName) {
        return homelessServiceService.filterByCategoryName(categoryName);
    }

    // Filter services by city name, category name, and time range
    @GetMapping("/filter")
    public List<HomelessService> filterByCityCategoryAndTime(
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        LocalTime parsedStartTime = (startTime != null) ? LocalTime.parse(startTime) : null;
        LocalTime parsedEndTime = (endTime != null) ? LocalTime.parse(endTime) : null;

        return homelessServiceService.filterByCityCategoryAndTime(cityName, categoryName, parsedStartTime, parsedEndTime);
    }
}
