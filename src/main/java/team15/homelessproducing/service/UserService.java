package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.User;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repos.UserRepository;
import team15.homelessproducing.repos.UserRoleRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Fetch all users
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch users", e);
        }
    }

    // Fetch a user by ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    // Create a new user with the default "User" role if not already set
    public User createUser(User user) {
        try {
            if (user.getRole() == null) {
                // Assign the default "User" role if none is provided
                UserRole userRole = userRoleRepository.findByRoleName("User")
                        .orElseThrow(() -> new ResourceNotFoundException("Default role 'User' not found"));
                user.setRole(userRole);
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DatabaseException("Failed to save user", e);
        }
    }

    // Update an existing user
    public User updateUser(int id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
                    if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
                    if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
                    if (updatedUser.getRole() != null) user.setRole(updatedUser.getRole());
                    try {
                        return userRepository.save(user);
                    } catch (Exception e) {
                        throw new DatabaseException("Failed to update user", e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete user", e);
        }
    }

    // Validate user credentials
    public User validateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    // Update user profile (partial updates)
    public User updateUserProfile(int id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getUsername() != null) {
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if (updatedUser.getEmail() != null) {
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getPassword() != null) {
                        existingUser.setPassword(updatedUser.getPassword());
                    }
                    try {
                        return userRepository.save(existingUser);
                    } catch (Exception e) {
                        throw new DatabaseException("Failed to update profile", e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }
}
