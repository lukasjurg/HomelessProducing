package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.AppService;
import team15.homelessproducing.model.City;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repos.AppServiceRepository;
import team15.homelessproducing.repos.CityRepository;
import team15.homelessproducing.repos.ServiceCategoryRepository;
import team15.homelessproducing.repos.VisitLogRepository;
import team15.homelessproducing.repos.FeedbackRepository;


import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appservices")
public class AppServiceController {

    @Autowired
    private AppServiceRepository appServiceRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private VisitLogRepository visitLogRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @GetMapping
    public ResponseEntity<List<AppService>> getAllAppServices(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        List<AppService> services;

        if (categoryName != null && cityName != null && startTime != null && endTime != null) {
            services = appServiceRepository.findByCategoryCityAndAvailability(categoryName, cityName, startTime, endTime);
        } else if (categoryName != null && cityName != null) {
            services = appServiceRepository.findByCategoryAndCity(categoryName, cityName);
        } else if (categoryName != null && startTime != null && endTime != null) {
            services = appServiceRepository.findByCategoryAndAvailability(categoryName, startTime, endTime);
        } else if (cityName != null && startTime != null && endTime != null) {
            services = appServiceRepository.findByCityAndAvailability(cityName, startTime, endTime);
        } else if (categoryName != null) {
            services = appServiceRepository.findByCategoryName(categoryName);
        } else if (cityName != null) {
            services = appServiceRepository.findByCityName(cityName);
        } else if (startTime != null && endTime != null) {
            services = appServiceRepository.findAvailableServicesWithinHours(startTime, endTime);
        } else {
            services = appServiceRepository.findAll();
        }

        return services.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppService> getAppServiceById(@PathVariable Integer id) {
        return appServiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppService> createAppService(@RequestBody AppService service) {
        if (service == null || service.getName() == null || service.getAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        AppService createdService = appServiceRepository.save(service);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppService> updateAppService(@PathVariable Integer id, @RequestBody AppService updatedService) {
        return appServiceRepository.findById(id)
                .map(existingService -> {
                    // Update primitive fields
                    if (updatedService.getName() != null) existingService.setName(updatedService.getName());
                    if (updatedService.getAddress() != null) existingService.setAddress(updatedService.getAddress());
                    if (updatedService.getContactNumber() != null) existingService.setContactNumber(updatedService.getContactNumber());
                    if (updatedService.getStartTime() != null) existingService.setStartTime(updatedService.getStartTime());
                    if (updatedService.getEndTime() != null) existingService.setEndTime(updatedService.getEndTime());

                    // Update city if provided
                    if (updatedService.getCity() != null && updatedService.getCity().getCityId() != 0) {
                        int cityId = updatedService.getCity().getCityId();
                        City city = cityRepository.findById(cityId)
                                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));
                        existingService.setCity(city);
                    }

                    // Update category if provided
                    if (updatedService.getCategory() != null && updatedService.getCategory().getCategoryId() != 0) {
                        int categoryId = updatedService.getCategory().getCategoryId();
                        ServiceCategory category = serviceCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
                        existingService.setCategory(category);
                    }

                    // Save the updated service
                    AppService savedService = appServiceRepository.save(existingService);
                    return ResponseEntity.ok(savedService);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppService(@PathVariable Integer id) {
        if (appServiceRepository.existsById(id)) {
            visitLogRepository.deleteByService_ServiceId(id); // Now resolved
            feedbackRepository.deleteByService_ServiceId(id); // Now resolved

            appServiceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
