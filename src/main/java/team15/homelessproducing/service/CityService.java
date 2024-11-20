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
        try {
            return cityRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve cities", e);
        }
    }

    public City getCityById(int id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    public City createCity(City city) {
        try {
            return cityRepository.save(city);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create city", e);
        }
    }

    public City updateCity(int id, City updatedCity) {
        return cityRepository.findById(id).map(city -> {
            city.setCityName(updatedCity.getCityName());
            try {
                return cityRepository.save(city);
            } catch (Exception e) {
                throw new DatabaseException("Failed to update city", e);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("City with ID " + id + " not found"));
    }

    public void deleteCity(int id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City with ID " + id + " not found");
        }
        try {
            cityRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete city", e);
        }
    }
}
