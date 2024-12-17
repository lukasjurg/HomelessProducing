package team15.homelessproducing.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CommunityViewController {

    @FXML
    private VBox forumContainer;

    // Representation of a community post
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommunityPost {
        private String postTitle;
        private String postContent;
        private String createdAt;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private User user; // User object inside CommunityPost

        // Getters and setters
        public String getPostTitle() { return postTitle; }
        public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

        public String getPostContent() { return postContent; }
        public void setPostContent(String postContent) { this.postContent = postContent; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
    }

    // Representation of a User
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        @JsonProperty("username") // Map 'username' from JSON to this field
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @FXML
    public void initialize() {
        System.out.println("Community View Loaded.");
        fetchCommunityPosts();
    }

    private void fetchCommunityPosts() {
        String apiUrl = "http://localhost:8080/api/community-posts"; // Replace with your backend endpoint

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateForumContainer)
                .exceptionally(e -> {
                    System.err.println("Error fetching posts: " + e.getMessage());
                    return null;
                });
    }

    private void updateForumContainer(String jsonResponse) {
        try {
            // Convert JSON to List of CommunityPost objects
            ObjectMapper mapper = new ObjectMapper();
            List<CommunityPost> posts = mapper.readValue(jsonResponse, new TypeReference<List<CommunityPost>>() {});

            Platform.runLater(() -> {
                forumContainer.getChildren().clear(); // Clear existing content

                for (CommunityPost post : posts) {
                    System.out.println("Post Title: " + post.getPostTitle());
                    System.out.println("User: " + (post.getUser() != null ? post.getUser().getName() : "null"));

                    VBox postBox = new VBox(5);
                    postBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 8; -fx-padding: 10; -fx-background-color: #f9f9f9;");

                    Label titleLabel = new Label(post.getPostTitle());
                    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    Label contentLabel = new Label(post.getPostContent());
                    contentLabel.setWrapText(true);
                    contentLabel.setStyle("-fx-font-size: 14px;");

                    Label createdAtLabel = new Label("Created at: " + post.getCreatedAt());
                    createdAtLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

                    // Ensure the user name is properly displayed
                    String userName = "Anonymous";
                    if (post.getUser() != null && post.getUser().getName() != null) {
                        userName = post.getUser().getName();
                    }
                    Label userLabel = new Label("Posted by: " + userName);
                    userLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

                    postBox.getChildren().addAll(titleLabel, contentLabel, createdAtLabel, userLabel);
                    forumContainer.getChildren().add(postBox);
                }
            });
        } catch (Exception e) {
            System.err.println("Error processing JSON response: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
