package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.City;
import team15.homelessproducing.repository.CityRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
    }

    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity); // Return 201 Created
    }


    @PutMapping("/{id}")
    public City updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        City city = cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        city.setCityName(cityDetails.getCityName());
        return cityRepository.save(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityRepository.deleteById(id);
    }

    @GetMapping("/names")
    public List<String> getAllCityNames() {
        return cityRepository.findAll().stream()
                .map(City::getCityName)
                .toList(); // Returns only city names
    }

}
