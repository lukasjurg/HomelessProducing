package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.Feedback;
import team15.homelessproducing.model.HomelessService;
import team15.homelessproducing.model.User;
import team15.homelessproducing.repository.FeedbackRepository;
import team15.homelessproducing.repository.HomelessServiceRepository;
import team15.homelessproducing.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomelessServiceRepository serviceRepository;

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        User user = userRepository.findById(feedback.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + feedback.getUser().getUserId()));

        HomelessService service = serviceRepository.findById(feedback.getService().getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + feedback.getService().getServiceId()));

        feedback.setUser(user);
        feedback.setService(service);

        return feedbackRepository.save(feedback);
    }


    @PutMapping("/{id}")
    public Feedback updateFeedback(@PathVariable Long id, @RequestBody Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setRating(feedbackDetails.getRating());
        feedback.setUser(feedbackDetails.getUser());
        feedback.setService(feedbackDetails.getService());
        return feedbackRepository.save(feedback);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
    }
}
