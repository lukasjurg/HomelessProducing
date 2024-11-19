package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessapp.model.ServiceAvailability;
import team15.homelessapp.repos.ServiceAvailabilityRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/serviceavailability")
public class ServiceAvailabilityController {

    @Autowired
    private ServiceAvailabilityRepository serviceAvailabilityRepository;

    @GetMapping
    public List<ServiceAvailability> getAllServiceAvailabilities() {
        return serviceAvailabilityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceAvailability> getServiceAvailabilityById(@PathVariable Integer id) {
        Optional<ServiceAvailability> availability = serviceAvailabilityRepository.findById(id);
        return availability.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceAvailability createServiceAvailability(@RequestBody ServiceAvailability availability) {
        availability.setLast_updated(new Date());  // Set last_updated to the current date and time
        return serviceAvailabilityRepository.save(availability);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceAvailability> updateServiceAvailability(@PathVariable Integer id, @RequestBody ServiceAvailability updatedAvailability) {
        return serviceAvailabilityRepository.findById(id)
                .map(avail -> {
                    avail.setService(updatedAvailability.getService());
                    avail.setAvailable_slots(updatedAvailability.getAvailable_slots());
                    avail.setLast_updated(new Date());  // Update last_updated to the current date and time
                    return ResponseEntity.ok(serviceAvailabilityRepository.save(avail));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceAvailability(@PathVariable Integer id) {
        if (serviceAvailabilityRepository.existsById(id)) {
            serviceAvailabilityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
