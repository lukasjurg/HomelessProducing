package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.ServiceAvailability;
import team15.homelessproducing.repository.ServiceAvailabilityRepository;

import java.util.List;

@Service
public class ServiceAvailabilityService {

    @Autowired
    private ServiceAvailabilityRepository serviceAvailabilityRepository;

    public List<ServiceAvailability> getAllServiceAvailability() {
        return serviceAvailabilityRepository.findAll();
    }

    public ServiceAvailability getServiceAvailabilityById(Long id) {
        return serviceAvailabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceAvailability not found"));
    }

    public ServiceAvailability createServiceAvailability(ServiceAvailability serviceAvailability) {
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    public ServiceAvailability updateServiceAvailability(Long id, ServiceAvailability serviceAvailabilityDetails) {
        ServiceAvailability serviceAvailability = serviceAvailabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceAvailability not found"));
        serviceAvailability.setAvailableSlots(serviceAvailabilityDetails.getAvailableSlots());
        serviceAvailability.setLastUpdated(serviceAvailabilityDetails.getLastUpdated());
        serviceAvailability.setService(serviceAvailabilityDetails.getService());
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    public void deleteServiceAvailability(Long id) {
        serviceAvailabilityRepository.deleteById(id);
    }
}
