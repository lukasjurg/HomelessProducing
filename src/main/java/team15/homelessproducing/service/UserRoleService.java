package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repos.UserRoleRepository;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Retrieve all roles
    public List<UserRole> getAllRoles() {
        try {
            return userRoleRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve user roles", e);
        }
    }

    // Retrieve a role by ID
    public UserRole getRoleById(int id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole with ID " + id + " not found"));
    }

    // Retrieve a role by name
    public UserRole getRoleByName(String roleName) {
        return userRoleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole with name '" + roleName + "' not found"));
    }

    // Create a new role
    public UserRole createRole(UserRole role) {
        try {
            return userRoleRepository.save(role);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create user role", e);
        }
    }

    // Update an existing role
    public UserRole updateRole(int id, UserRole updatedRole) {
        return userRoleRepository.findById(id).map(role -> {
            role.setRole_name(updatedRole.getRole_name());
            try {
                return userRoleRepository.save(role);
            } catch (Exception e) {
                throw new DatabaseException("Failed to update user role", e);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("UserRole with ID " + id + " not found"));
    }

    // Delete a role by ID
    public void deleteRole(int id) {
        if (!userRoleRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserRole with ID " + id + " not found");
        }
        try {
            userRoleRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete user role", e);
        }
    }
}
