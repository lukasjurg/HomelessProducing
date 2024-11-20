package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.AppService;
import team15.homelessproducing.repos.AppServiceRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appservices")
public class AppServiceController {

    @Autowired
    private AppServiceRepository appServiceRepository;

    @GetMapping
    public ResponseEntity<List<AppService>> getAllAppServices(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        List<AppService> services;
        if (categoryName != null && startTime != null && endTime != null) {
            services = appServiceRepository.findByCategoryAndAvailability(categoryName, startTime, endTime);
        } else if (categoryName != null) {
            services = appServiceRepository.findByCategoryName(categoryName);
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
                    existingService.setName(updatedService.getName());
                    existingService.setAddress(updatedService.getAddress());
                    existingService.setContactNumber(updatedService.getContactNumber());
                    existingService.setStartTime(updatedService.getStartTime());
                    existingService.setEndTime(updatedService.getEndTime());
                    existingService.setCity(updatedService.getCity());
                    existingService.setCategory(updatedService.getCategory());
                    AppService savedService = appServiceRepository.save(existingService);
                    return ResponseEntity.ok(savedService);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppService(@PathVariable Integer id) {
        if (appServiceRepository.existsById(id)) {
            appServiceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
