package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessapp.exceptions.DatabaseException;
import team15.homelessapp.exceptions.ResourceNotFoundException;
import team15.homelessapp.model.ServiceAvailability;
import team15.homelessapp.repos.ServiceAvailabilityRepository;

import java.util.List;

@Service
public class ServiceAvailabilityService {

    @Autowired
    private ServiceAvailabilityRepository serviceAvailabilityRepository;

    public List<ServiceAvailability> getAllServiceAvailabilities() {
        try {
            return serviceAvailabilityRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve service availabilities", e);
        }
    }

    public ServiceAvailability getServiceAvailabilityById(int id) {
        return serviceAvailabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceAvailability with ID " + id + " not found"));
    }

    public ServiceAvailability createServiceAvailability(ServiceAvailability availability) {
        try {
            return serviceAvailabilityRepository.save(availability);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create service availability", e);
        }
    }

    public ServiceAvailability updateServiceAvailability(int id, ServiceAvailability updatedAvailability) {
        return serviceAvailabilityRepository.findById(id).map(availability -> {
            availability.setService(updatedAvailability.getService());
            availability.setAvailable_slots(updatedAvailability.getAvailable_slots());
            availability.setLast_updated(updatedAvailability.getLast_updated());
            try {
                return serviceAvailabilityRepository.save(availability);
            } catch (Exception e) {
                throw new DatabaseException("Failed to update service availability", e);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("ServiceAvailability with ID " + id + " not found"));
    }

    public void deleteServiceAvailability(int id) {
        if (!serviceAvailabilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("ServiceAvailability with ID " + id + " not found");
        }
        try {
            serviceAvailabilityRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete service availability", e);
        }
    }
}
