package team15.homelessproducing.model;

import javax.persistence.*;

@Entity
@Table(name = "City")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int city_ID;

    private String city_name;

    // Getters and Setters
    public int getCity_ID() {
        return city_ID;
    }

    public void setCity_ID(int city_ID) {
        this.city_ID = city_ID;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
