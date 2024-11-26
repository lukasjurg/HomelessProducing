package team15.homelessproducing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email") // Ensure email uniqueness
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Prevent serialization issues with lazy loading
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id; // Renamed to match the database schema

    @NotBlank(message = "Username is required")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true) // Ensure unique email
    private String email;

    @ManyToOne(fetch = FetchType.LAZY) // Relationship with UserRole
    @JoinColumn(name = "role_id", nullable = false) // Foreign key column
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Prevent lazy loading serialization issues for role
    private UserRole role;

    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Utility method for password validation
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Utility method for masking sensitive information
    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + (role != null ? role.getRole_name() : "null") +
                '}';
    }
}
