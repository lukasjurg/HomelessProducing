package team15.homelessproducing.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.City;
import team15.homelessproducing.model.HomelessService;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repository.CityRepository;
import team15.homelessproducing.repository.HomelessServiceRepository;
import team15.homelessproducing.repository.ServiceCategoryRepository;


import java.time.LocalTime;
import java.util.List;

@Service
public class HomelessServiceService {

    @Autowired
    private HomelessServiceRepository homelessServiceRepository;
    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    private CityRepository cityRepository;

    public List<HomelessService> getAllHomelessServices() {
        return homelessServiceRepository.findAll();
    }

    public HomelessService getHomelessServiceById(Long id) {
        return homelessServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HomelessService not found"));
    }

    @Transactional
    public HomelessService createHomelessService(HomelessService homelessService) {
        // Fetch the category and city from the database
        ServiceCategory category = serviceCategoryRepository.findById(homelessService.getCategory().getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory not found with id: " + homelessService.getCategory().getCategoryId()));

        City city = cityRepository.findById(homelessService.getCity().getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + homelessService.getCity().getCityId()));

        // Set the managed entities
        homelessService.setCategory(category);
        homelessService.setCity(city);

        // Save the homelessService
        return homelessServiceRepository.save(homelessService);
    }


    public HomelessService updateHomelessService(Long id, HomelessService homelessServiceDetails) {
        HomelessService homelessService = homelessServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HomelessService not found"));
        homelessService.setName(homelessServiceDetails.getName());
        homelessService.setAddress(homelessServiceDetails.getAddress());
        homelessService.setContactNumber(homelessServiceDetails.getContactNumber());
        homelessService.setStartTime(homelessServiceDetails.getStartTime());
        homelessService.setEndTime(homelessServiceDetails.getEndTime());
        homelessService.setCity(homelessServiceDetails.getCity());
        homelessService.setCategory(homelessServiceDetails.getCategory());
        return homelessServiceRepository.save(homelessService);
    }

    public void deleteHomelessService(Long id) {
        homelessServiceRepository.deleteById(id);
    }

    public List<HomelessService> filterByTimeRange(LocalTime start, LocalTime end) {
        return homelessServiceRepository.findByTimeRange(start, end);
    }

    public List<HomelessService> filterByCityName(String cityName) {
        return homelessServiceRepository.findByCityName(cityName);
    }

    public List<HomelessService> filterByCategoryName(String categoryName) {
        return homelessServiceRepository.findByCategoryName(categoryName);
    }

    public List<HomelessService> filterByCityCategoryAndTime(String cityName, String categoryName, LocalTime startTime, LocalTime endTime) {
        return homelessServiceRepository.filterByCityCategoryAndTime(cityName, categoryName, startTime, endTime);
    }
}
