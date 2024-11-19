package team15.homelessproducing.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "VisitLog")
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int visit_ID;

    @ManyToOne
    @JoinColumn(name = "user_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_ID", nullable = false)
    private AppService service; // Updated to AppService

    @Temporal(TemporalType.TIMESTAMP)
    private Date visit_date;

    private String visit_notes;

    // Getters and Setters
    public int getVisit_ID() {
        return visit_ID;
    }

    public void setVisit_ID(int visit_ID) {
        this.visit_ID = visit_ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppService getService() { // Updated to AppService
        return service;
    }

    public void setService(AppService service) { // Updated to AppService
        this.service = service;
    }

    public Date getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(Date visit_date) {
        this.visit_date = visit_date;
    }

    public String getVisit_notes() {
        return visit_notes;
    }

    public void setVisit_notes(String visit_notes) {
        this.visit_notes = visit_notes;
    }
}
