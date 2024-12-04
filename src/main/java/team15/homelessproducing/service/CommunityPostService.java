package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.CommunityPost;
import team15.homelessproducing.repository.CommunityPostRepository;

import java.util.List;

@Service
public class CommunityPostService {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    public List<CommunityPost> getAllCommunityPosts() {
        return communityPostRepository.findAll();
    }

    public CommunityPost getCommunityPostById(Long id) {
        return communityPostRepository.findById(id).orElseThrow(() -> new RuntimeException("CommunityPost not found"));
    }

    public CommunityPost createCommunityPost(CommunityPost communityPost) {
        return communityPostRepository.save(communityPost);
    }

    public CommunityPost updateCommunityPost(Long id, CommunityPost communityPostDetails) {
        CommunityPost communityPost = communityPostRepository.findById(id).orElseThrow(() -> new RuntimeException("CommunityPost not found"));
        communityPost.setPostTitle(communityPostDetails.getPostTitle());
        communityPost.setPostContent(communityPostDetails.getPostContent());
        communityPost.setCreatedAt(communityPostDetails.getCreatedAt());
        communityPost.setUpdatedAt(communityPostDetails.getUpdatedAt());
        communityPost.setUser(communityPostDetails.getUser());
        return communityPostRepository.save(communityPost);
    }

    public void deleteCommunityPost(Long id) {
        communityPostRepository.deleteById(id);
    }
}
