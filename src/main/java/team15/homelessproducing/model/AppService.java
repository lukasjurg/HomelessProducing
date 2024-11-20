package team15.homelessproducing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "service")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int serviceId;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnoreProperties("services") // Prevents infinite recursion if bidirectional mapping exists
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("services") // Prevents infinite recursion if bidirectional mapping exists
    private ServiceCategory category;

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "AppService{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", city=" + (city != null ? city.getCityName() : "N/A") +
                ", category=" + (category != null ? category.getCategoryName() : "N/A") +
                '}';
    }
}
