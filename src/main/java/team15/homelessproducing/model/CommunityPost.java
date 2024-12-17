package team15.homelessproducing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String postTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String postContent;

    private String createdAt;
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Prevent circular references
    private User user;

    @JsonProperty("username") // Explicitly map "username" in the JSON response
    public String getUsername() {
        return user != null ? user.getUsername() : "Anonymous"; // Fetch username without changing User class
    }

    // Getters and Setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @JsonIgnore
    public User getUser() { return user; } // Keep user serialization ignored for direct user reference
    public void setUser(User user) { this.user = user; }
}
