package team15.homelessproducing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "city")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private int cityId;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("city") // Prevents infinite recursion when serializing
    private List<AppService> services;

    // Getters and Setters
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AppService> getServices() {
        return services;
    }

    public void setServices(List<AppService> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
