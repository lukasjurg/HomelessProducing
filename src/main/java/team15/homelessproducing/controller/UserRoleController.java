package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repository.UserRoleRepository;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    @GetMapping("/{id}")
    public UserRole getUserRoleById(@PathVariable Long id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
    }

    @PostMapping
    public UserRole createUserRole(@RequestBody UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @PutMapping("/{id}")
    public UserRole updateUserRole(@PathVariable Long id, @RequestBody UserRole userRoleDetails) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
        userRole.setRoleName(userRoleDetails.getRoleName());
        return userRoleRepository.save(userRole);
    }

    @DeleteMapping("/{id}")
    public void deleteUserRole(@PathVariable Long id) {
        userRoleRepository.deleteById(id);
    }
}
