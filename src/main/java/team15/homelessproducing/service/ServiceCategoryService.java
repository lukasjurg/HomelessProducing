package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repository.ServiceCategoryRepository;

import java.util.List;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }

    public ServiceCategory getServiceCategoryById(Long id) {
        return serviceCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceCategory not found"));
    }

    public ServiceCategory createServiceCategory(ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    public ServiceCategory updateServiceCategory(Long id, ServiceCategory serviceCategoryDetails) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceCategory not found"));
        serviceCategory.setCategoryName(serviceCategoryDetails.getCategoryName());
        serviceCategory.setCategoryDescription(serviceCategoryDetails.getCategoryDescription());
        return serviceCategoryRepository.save(serviceCategory);
    }

    public void deleteServiceCategory(Long id) {
        serviceCategoryRepository.deleteById(id);
    }
}
