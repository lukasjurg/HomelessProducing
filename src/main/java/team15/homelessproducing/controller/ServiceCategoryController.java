package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.ServiceCategory;
import team15.homelessproducing.repos.ServiceCategoryRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicecategories")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllCategories() {
        List<ServiceCategory> categories = serviceCategoryRepository.findAll();
        return categories.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getCategoryById(@PathVariable Integer id) {
        return serviceCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceCategory> createCategory(@RequestBody ServiceCategory category) {
        if (category == null || category.getCategoryName() == null) {
            return ResponseEntity.badRequest().build();
        }
        ServiceCategory createdCategory = serviceCategoryRepository.save(category);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> updateCategory(@PathVariable Integer id, @RequestBody ServiceCategory updatedCategory) {
        return serviceCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setCategoryName(updatedCategory.getCategoryName());
                    existingCategory.setCategoryDescription(updatedCategory.getCategoryDescription());
                    ServiceCategory savedCategory = serviceCategoryRepository.save(existingCategory);
                    return ResponseEntity.ok(savedCategory);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (serviceCategoryRepository.existsById(id)) {
            serviceCategoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
