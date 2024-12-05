package team15.homelessproducing.model;

import jakarta.persistence.*;

@Entity
public class ServiceAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availabilityId;

    private int availableSlots;
    private String lastUpdated;

    @OneToOne
    @JoinColumn(name = "service_id")
    private HomelessService service;

    // Getters and Setters
    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public HomelessService getService() {
        return service;
    }

    public void setService(HomelessService service) {
        this.service = service;
    }
}
