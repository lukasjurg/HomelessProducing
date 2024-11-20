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

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch users", e);
        }
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    public User createUser(User user) {
        // Fetch the "User" role
        UserRole userRole = userRoleRepository.findByRoleName("User")
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'User' not found"));

        // Assign the "User" role to the new user
        user.setRole(userRole);

        // Save the user
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to save user", e);
        }
    }

    public User updateUser(int id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setPassword(updatedUser.getPassword());
                    user.setEmail(updatedUser.getEmail());
                    user.setRole(updatedUser.getRole());
                    try {
                        return userRepository.save(user);
                    } catch (Exception e) {
                        throw new DatabaseException("Failed to update user", e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

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
}
