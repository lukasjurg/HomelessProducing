package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.City;
import team15.homelessproducing.repos.CityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Integer id) {
        return cityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        if (city == null || city.getCityName() == null || city.getCityName().isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank.");
        }
        City createdCity = cityRepository.save(city);
        return ResponseEntity.ok(createdCity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Integer id, @RequestBody City updatedCity) {
        return cityRepository.findById(id)
                .map(existingCity -> {
                    existingCity.setCityName(updatedCity.getCityName());
                    City savedCity = cityRepository.save(existingCity);
                    return ResponseEntity.ok(savedCity);
                })
                .orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City with ID " + id + " not found");
        }
        cityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
