package team15.homelessproducing.model;

import javax.persistence.*;

@Entity
@Table(name = "ServiceCategory")
public class ServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_ID;

    private String category_name;
    private String category_description;

    // Constructor
    public ServiceCategory() {}

    // Getters and Setters
    public int getCategory_ID() {
        return category_ID;
    }

    public void setCategory_ID(int category_ID) {
        this.category_ID = category_ID;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }
}
