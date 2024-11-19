package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repos.ServiceCategoryRepository;

import java.util.List;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public List<ServiceCategory> getAllServiceCategories() {
        try {
            return serviceCategoryRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve service categories", e);
        }
    }

    public ServiceCategory getServiceCategoryById(int id) {
        return serviceCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceCategory with ID " + id + " not found"));
    }

    public ServiceCategory createServiceCategory(ServiceCategory category) {
        try {
            return serviceCategoryRepository.save(category);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create service category", e);
        }
    }

    public ServiceCategory updateServiceCategory(int id, ServiceCategory updatedCategory) {
        return serviceCategoryRepository.findById(id).map(category -> {
            category.setCategory_name(updatedCategory.getCategory_name());
            category.setCategory_description(updatedCategory.getCategory_description());
            try {
                return serviceCategoryRepository.save(category);
            } catch (Exception e) {
                throw new DatabaseException("Failed to update service category", e);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("ServiceCategory with ID " + id + " not found"));
    }

    public void deleteServiceCategory(int id) {
        if (!serviceCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("ServiceCategory with ID " + id + " not found");
        }
        try {
            serviceCategoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete service category", e);
        }
    }
}
