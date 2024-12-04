package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repository.ServiceCategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/service-categories")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @GetMapping
    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ServiceCategory getServiceCategoryById(@PathVariable Long id) {
        return serviceCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceCategory not found"));
    }

    @PostMapping
    public ServiceCategory createServiceCategory(@RequestBody ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    @PutMapping("/{id}")
    public ServiceCategory updateServiceCategory(@PathVariable Long id, @RequestBody ServiceCategory serviceCategoryDetails) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceCategory not found"));
        serviceCategory.setCategoryName(serviceCategoryDetails.getCategoryName());
        serviceCategory.setCategoryDescription(serviceCategoryDetails.getCategoryDescription());
        return serviceCategoryRepository.save(serviceCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteServiceCategory(@PathVariable Long id) {
        serviceCategoryRepository.deleteById(id);
    }
}
