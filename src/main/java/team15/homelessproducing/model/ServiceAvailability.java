package team15.homelessproducing.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ServiceAvailability")
public class ServiceAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availability_ID;

    @ManyToOne
    @JoinColumn(name = "service_ID", nullable = false)
    private AppService service; // Updated to AppService

    private int available_slots;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_updated;

    // Getters and Setters

    public int getAvailability_ID() {
        return availability_ID;
    }

    public void setAvailability_ID(int availability_ID) {
        this.availability_ID = availability_ID;
    }

    public AppService getService() { // Updated to AppService
        return service;
    }

    public void setService(AppService service) { // Updated to AppService
        this.service = service;
    }

    public int getAvailable_slots() {
        return available_slots;
    }

    public void setAvailable_slots(int available_slots) {
        this.available_slots = available_slots;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}
