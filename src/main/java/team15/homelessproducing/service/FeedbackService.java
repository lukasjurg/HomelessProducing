package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.exceptions.DatabaseException;
import team15.homelessproducing.exceptions.ResourceNotFoundException;
import team15.homelessproducing.model.Feedback;
import team15.homelessproducing.repos.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        try {
            return feedbackRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch feedback", e);
        }
    }

    public Feedback getFeedbackById(int id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback with ID " + id + " not found"));
    }

    public Feedback createFeedback(Feedback feedback) {
        try {
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new DatabaseException("Failed to save feedback", e);
        }
    }

    public Feedback updateFeedback(int id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setUser(updatedFeedback.getUser());
                    feedback.setService(updatedFeedback.getService());
                    feedback.setRating(updatedFeedback.getRating());
                    try {
                        return feedbackRepository.save(feedback);
                    } catch (Exception e) {
                        throw new DatabaseException("Failed to update feedback", e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Feedback with ID " + id + " not found"));
    }

    public void deleteFeedback(int id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback with ID " + id + " not found");
        }
        try {
            feedbackRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete feedback", e);
        }
    }
}
