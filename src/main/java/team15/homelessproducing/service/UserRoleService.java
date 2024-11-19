package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessapp.exceptions.DatabaseException;
import team15.homelessapp.exceptions.ResourceNotFoundException;
import team15.homelessapp.model.UserRole;
import team15.homelessapp.repos.UserRoleRepository;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> getAllRoles() {
        try {
            return userRoleRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve user roles", e);
        }
    }

    public UserRole getRoleById(int id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole with ID " + id + " not found"));
    }

    public UserRole createRole(UserRole role) {
        try {
            return userRoleRepository.save(role);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create user role", e);
        }
    }

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
