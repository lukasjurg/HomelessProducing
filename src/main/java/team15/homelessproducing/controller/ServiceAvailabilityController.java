package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.ServiceAvailability;
import team15.homelessproducing.repository.ServiceAvailabilityRepository;

import java.util.List;

@RestController
@RequestMapping("/api/service-availability")
public class ServiceAvailabilityController {

    @Autowired
    private ServiceAvailabilityRepository serviceAvailabilityRepository;

    @GetMapping
    public List<ServiceAvailability> getAllServiceAvailability() {
        return serviceAvailabilityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ServiceAvailability getServiceAvailabilityById(@PathVariable Long id) {
        return serviceAvailabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceAvailability not found"));
    }

    @PostMapping
    public ServiceAvailability createServiceAvailability(@RequestBody ServiceAvailability serviceAvailability) {
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    @PutMapping("/{id}")
    public ServiceAvailability updateServiceAvailability(@PathVariable Long id, @RequestBody ServiceAvailability serviceAvailabilityDetails) {
        ServiceAvailability serviceAvailability = serviceAvailabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceAvailability not found"));
        serviceAvailability.setAvailableSlots(serviceAvailabilityDetails.getAvailableSlots());
        serviceAvailability.setLastUpdated(serviceAvailabilityDetails.getLastUpdated());
        serviceAvailability.setService(serviceAvailabilityDetails.getService());
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    @DeleteMapping("/{id}")
    public void deleteServiceAvailability(@PathVariable Long id) {
        serviceAvailabilityRepository.deleteById(id);
    }
}
