package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.City;
import team15.homelessproducing.repos.CityRepository;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(int id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    public City createCity(City city) {
        if (city == null || city.getCityName() == null || city.getCityName().isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank.");
        }
        return cityRepository.save(city);
    }

    public City updateCity(int id, City updatedCity) {
        return cityRepository.findById(id)
                .map(city -> {
                    city.setCityName(updatedCity.getCityName());
                    return cityRepository.save(city);
                })
                .orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    public void deleteCity(int id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City with ID " + id + " not found");
        }
        cityRepository.deleteById(id);
    }
}
