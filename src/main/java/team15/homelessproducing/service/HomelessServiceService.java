package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.HomelessService;
import team15.homelessproducing.repository.HomelessServiceRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class HomelessServiceService {

    @Autowired
    private HomelessServiceRepository homelessServiceRepository;

    public List<HomelessService> getAllHomelessServices() {
        return homelessServiceRepository.findAll();
    }

    public HomelessService getHomelessServiceById(Long id) {
        return homelessServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HomelessService not found"));
    }

    public HomelessService createHomelessService(HomelessService homelessService) {
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
