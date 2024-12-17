package team15.homelessproducing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.CommunityPost;
import team15.homelessproducing.model.User;
import team15.homelessproducing.repository.CommunityPostRepository;
import team15.homelessproducing.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/community-posts")
public class CommunityPostController {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommunityPostController.class);

    @GetMapping
    public List<CommunityPost> getAllCommunityPosts() {
        return communityPostRepository.findAll();
    }

    @PostMapping
    public CommunityPost createCommunityPost(@RequestBody CommunityPost communityPost) {
        logger.info("Incoming CommunityPost: Title={}, Content={}, UserId={}",
                communityPost.getPostTitle(),
                communityPost.getPostContent(),
                communityPost.getUser() != null ? communityPost.getUser().getUserId() : "null");

        if (communityPost.getUser() == null || communityPost.getUser().getUserId() == null) {
            throw new RuntimeException("User ID is required to create a post.");
        }

        User user = userRepository.findById(communityPost.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + communityPost.getUser().getUserId()));

        communityPost.setUser(user);
        communityPost.setCreatedAt(LocalDateTime.now().toString());
        communityPost.setUpdatedAt(LocalDateTime.now().toString());
        return communityPostRepository.save(communityPost);
    }
}
