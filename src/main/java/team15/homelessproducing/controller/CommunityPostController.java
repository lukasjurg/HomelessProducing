package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.CommunityPost;
import team15.homelessproducing.repository.CommunityPostRepository;

import java.util.List;

@RestController
@RequestMapping("/api/community-posts")
public class CommunityPostController {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @GetMapping
    public List<CommunityPost> getAllCommunityPosts() {
        return communityPostRepository.findAll();
    }

    @GetMapping("/{id}")
    public CommunityPost getCommunityPostById(@PathVariable Long id) {
        return communityPostRepository.findById(id).orElseThrow(() -> new RuntimeException("CommunityPost not found"));
    }

    @PostMapping
    public CommunityPost createCommunityPost(@RequestBody CommunityPost communityPost) {
        return communityPostRepository.save(communityPost);
    }

    @PutMapping("/{id}")
    public CommunityPost updateCommunityPost(@PathVariable Long id, @RequestBody CommunityPost communityPostDetails) {
        CommunityPost communityPost = communityPostRepository.findById(id).orElseThrow(() -> new RuntimeException("CommunityPost not found"));
        communityPost.setPostTitle(communityPostDetails.getPostTitle());
        communityPost.setPostContent(communityPostDetails.getPostContent());
        communityPost.setCreatedAt(communityPostDetails.getCreatedAt());
        communityPost.setUpdatedAt(communityPostDetails.getUpdatedAt());
        communityPost.setUser(communityPostDetails.getUser());
        return communityPostRepository.save(communityPost);
    }

    @DeleteMapping("/{id}")
    public void deleteCommunityPost(@PathVariable Long id) {
        communityPostRepository.deleteById(id);
    }
}
