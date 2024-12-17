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

    @GetMapping
    public List<HomelessService> getAllHomelessServices() {
        return homelessServiceService.getAllHomelessServices();
    }

    @GetMapping("/{id}")
    public HomelessService getHomelessServiceById(@PathVariable Long id) {
        return homelessServiceService.getHomelessServiceById(id);
    }

    @PostMapping
    public HomelessService createHomelessService(@RequestBody HomelessService homelessService) {
        return homelessServiceService.createHomelessService(homelessService);
    }

    @PutMapping("/{id}")
    public HomelessService updateHomelessService(@PathVariable Long id, @RequestBody HomelessService homelessServiceDetails) {
        return homelessServiceService.updateHomelessService(id, homelessServiceDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteHomelessService(@PathVariable Long id) {
        homelessServiceService.deleteHomelessService(id);
    }

    @GetMapping("/filter-by-time")
    public List<HomelessService> filterByTimeRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        return homelessServiceService.filterByTimeRange(startTime, endTime);
    }

    @GetMapping("/by-city")
    public List<HomelessService> filterByCityName(@RequestParam String cityName) {
        return homelessServiceService.filterByCityName(cityName);
    }

    @GetMapping("/by-category")
    public List<HomelessService> filterByCategoryName(@RequestParam String categoryName) {
        return homelessServiceService.filterByCategoryName(categoryName);
    }

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
