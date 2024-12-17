package team15.homelessproducing.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import team15.homelessproducing.util.UserSession;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CommunityViewController {

    @FXML
    private VBox forumContainer;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommunityPost {
        private String postTitle;
        private String postContent;
        private String createdAt;

        @JsonIgnoreProperties(ignoreUnknown = true)
        private User user;

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        @JsonProperty("username")
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @FXML
    public void initialize() {
        System.out.println("Community View Loaded.");
        addCreatePostButton();
        fetchCommunityPosts();
    }

    private void addCreatePostButton() {
        Platform.runLater(() -> {
            Button createPostButton = new Button("Create a Post");
            createPostButton.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-color: #4CAF50; -fx-text-fill: white;");

            createPostButton.setOnAction(event -> handleCreatePost());

            if (forumContainer.getChildren().isEmpty()) {
                forumContainer.getChildren().add(createPostButton);
            } else if (!(forumContainer.getChildren().get(0) instanceof Button)) {
                forumContainer.getChildren().add(0, createPostButton);
            }
        });
    }

    private void fetchCommunityPosts() {
        String apiUrl = "http://localhost:8080/api/community-posts";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateForumContainer)
                .exceptionally(e -> {
                    System.err.println("Error fetching posts: " + e.getMessage());
                    return null;
                });
    }

    private void updateForumContainer(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<CommunityPost> posts = mapper.readValue(jsonResponse, new TypeReference<List<CommunityPost>>() {});

            Platform.runLater(() -> {
                if (forumContainer.getChildren().size() > 1) {
                    forumContainer.getChildren().remove(1, forumContainer.getChildren().size());
                }

                for (CommunityPost post : posts) {
                    VBox postBox = new VBox(5);
                    postBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 8; -fx-padding: 10; -fx-background-color: #f9f9f9;");

                    Label titleLabel = new Label(post.getPostTitle());
                    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    Label contentLabel = new Label(post.getPostContent());
                    contentLabel.setWrapText(true);
                    contentLabel.setStyle("-fx-font-size: 14px;");

                    Label createdAtLabel = new Label("Created at: " + post.getCreatedAt());
                    createdAtLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

                    String userName = (post.getUser() != null && post.getUser().getName() != null)
                            ? post.getUser().getName()
                            : "Anonymous";
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

    @FXML
    public void handleCreatePost(ActionEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create a Post");
        dialog.setHeaderText("Share something with the community");

        TextField titleField = new TextField();
        titleField.setPromptText("Enter post title");
        TextArea contentField = new TextArea();
        contentField.setPromptText("Write your post here...");
        contentField.setWrapText(true);

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Post Title:"), titleField, new Label("Content:"), contentField);
        dialog.getDialogPane().setContent(content);

        ButtonType submitButton = new ButtonType("Post", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == submitButton && !titleField.getText().trim().isEmpty() && !contentField.getText().trim().isEmpty()) {
                sendPostToServer(titleField.getText(), contentField.getText());
            } else if (button == submitButton) {
                showAlert("Error", "Title and content cannot be empty.");
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void sendPostToServer(String title, String content) {
        Long userId = UserSession.getInstance().getCurrentUserId();
        if (userId == null) {
            showAlert("Error", "You must be logged in to create a post.");
            return;
        }

        String apiUrl = "http://localhost:8080/api/community-posts";
        String jsonPayload = String.format(
                "{\"postTitle\":\"%s\", \"postContent\":\"%s\", \"user\": {\"userId\": %d}}",
                title, content, userId
        );

        System.out.println("Sending JSON Payload: " + jsonPayload); // Debugging log

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    System.out.println("Response Status Code: " + statusCode); // Debugging log
                    if (statusCode == 201 || statusCode == 200) {
                        showAlert("Success", "Your post has been created successfully!");
                        fetchCommunityPosts(); // Refresh posts
                    } else {
                        showAlert("Error", "Failed to create post. Status Code: " + statusCode);
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error creating post: " + e.getMessage());
                    showAlert("Error", "An error occurred while creating the post.");
                    return null;
                });
    }


    @FXML
    private void handleCreatePost() {
        System.out.println("Create a Post button clicked.");
        showCreatePostDialog();
    }

    private void showCreatePostDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create a Post");
        dialog.setHeaderText("Share something with the community");

        TextField titleField = new TextField();
        titleField.setPromptText("Enter post title");

        TextArea contentField = new TextArea();
        contentField.setPromptText("Write your post here...");
        contentField.setWrapText(true);

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Post Title:"), titleField, new Label("Content:"), contentField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                System.out.println("Title: " + titleField.getText());
                System.out.println("Content: " + contentField.getText());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
