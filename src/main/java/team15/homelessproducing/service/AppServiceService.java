package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.AppService;
import team15.homelessproducing.repos.AppServiceRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class AppServiceService {

    @Autowired
    private AppServiceRepository appServiceRepository;

    public List<AppService> getAllServices() {
        try {
            return appServiceRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve services", e);
        }
    }

    public AppService getServiceById(int id) {
        return appServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service with ID " + id + " not found"));
    }

    public AppService createService(AppService service) {
        try {
            return appServiceRepository.save(service);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create service", e);
        }
    }

    public AppService updateService(int id, AppService updatedService) {
        return appServiceRepository.findById(id).map(service -> {
            service.setName(updatedService.getName());
            service.setAddress(updatedService.getAddress());
            service.setContactNumber(updatedService.getContactNumber());
            service.setStartTime(updatedService.getStartTime());
            service.setEndTime(updatedService.getEndTime());
            service.setCity(updatedService.getCity());
            service.setCategory(updatedService.getCategory());
            try {
                return appServiceRepository.save(service);
            } catch (Exception e) {
                throw new DatabaseException("Failed to update service", e);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("Service with ID " + id + " not found"));
    }

    public void deleteService(int id) {
        if (!appServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service with ID " + id + " not found");
        }
        try {
            appServiceRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete service", e);
        }
    }

    public List<AppService> getServicesAvailableFrom9To2() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(14, 0);
        try {
            return appServiceRepository.findAvailableServicesWithinHours(start, end);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve available services within specified hours", e);
        }
    }

    public List<AppService> getServicesByCategory(String categoryName) {
        try {
            return appServiceRepository.findByCategoryName(categoryName);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve services by category: " + categoryName, e);
        }
    }

    public List<AppService> getServicesByCity(String cityName) {
        try {
            return appServiceRepository.findByCityName(cityName);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve services by city: " + cityName, e);
        }
    }

    public List<AppService> getServicesByCategoryAndAvailability(String categoryName, LocalTime startTime, LocalTime endTime) {
        try {
            return appServiceRepository.findByCategoryAndAvailability(categoryName, startTime, endTime);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve services by category and availability", e);
        }
    }

    public List<AppService> getServicesByCityAndAvailability(String cityName, LocalTime startTime, LocalTime endTime) {
        try {
            return appServiceRepository.findByCityAndAvailability(cityName, startTime, endTime);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve services by city and availability", e);
        }
    }
}
