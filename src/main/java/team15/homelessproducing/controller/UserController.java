package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.Response;
import team15.homelessproducing.model.User;
import team15.homelessproducing.model.UserRole;
import team15.homelessproducing.repository.UserRepository;
import team15.homelessproducing.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists!";
        }
        UserRole defaultRole = userRoleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.setRole(defaultRole);
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public Response loginUser(@RequestBody User user) {
        System.out.println("Login attempt for username: " + user.getUsername());
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent() && foundUser.get().getPassword().equals(user.getPassword())) {
            User loggedInUser = foundUser.get();
            System.out.println("Login successful for username: " + loggedInUser.getUsername());
            return new Response(
                    "Login successful!",
                    loggedInUser.getRole().getRoleName(),
                    loggedInUser.getUserId(),
                    loggedInUser.getUsername() // Include username here
            );
        } else {
            System.out.println("Login failed for username: " + user.getUsername());
            return new Response("Invalid username or password!", null, null, null);
        }
    }

    @PutMapping("/self-update/{id}")
    public User selfUpdateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }
        return userRepository.save(existingUser);
    }
}
