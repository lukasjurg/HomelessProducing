package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repository.UserRoleRepository;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
    }

    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public UserRole updateUserRole(Long id, UserRole userRoleDetails) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
        userRole.setRoleName(userRoleDetails.getRoleName());
        return userRoleRepository.save(userRole);
    }

    public void deleteUserRole(Long id) {
        userRoleRepository.deleteById(id);
    }
}
