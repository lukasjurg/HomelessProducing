package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessapp.model.ServiceCategory;
import team15.homelessapp.repos.ServiceCategoryRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicecategories")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @GetMapping
    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getCategoryById(@PathVariable Integer id) {
        Optional<ServiceCategory> category = serviceCategoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceCategory createCategory(@RequestBody ServiceCategory category) {
        return serviceCategoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> updateCategory(@PathVariable Integer id, @RequestBody ServiceCategory updatedCategory) {
        return serviceCategoryRepository.findById(id)
                .map(cat -> {
                    cat.setCategory_name(updatedCategory.getCategory_name());
                    cat.setCategory_description(updatedCategory.getCategory_description());
                    return ResponseEntity.ok(serviceCategoryRepository.save(cat));
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
