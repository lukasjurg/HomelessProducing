package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repos.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRole> getRoleById(@PathVariable Integer id) {
        Optional<UserRole> role = userRoleRepository.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserRole createRole(@RequestBody UserRole role) {
        return userRoleRepository.save(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRole> updateRole(@PathVariable Integer id, @RequestBody UserRole updatedRole) {
        return userRoleRepository.findById(id)
                .map(role -> {
                    role.setRole_name(updatedRole.getRole_name());
                    return ResponseEntity.ok(userRoleRepository.save(role));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        if (userRoleRepository.existsById(id)) {
            userRoleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
