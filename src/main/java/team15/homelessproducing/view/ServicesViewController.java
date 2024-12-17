package team15.homelessproducing.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import team15.homelessproducing.model.Feedback;
import team15.homelessproducing.util.JsonUtils;
import team15.homelessproducing.util.UserSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ServicesViewController {

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private CheckBox openedNowCheckBox;

    private List<Feedback> feedbackList;

    @FXML
    private VBox servicesContainer;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private List<HomelessService> allServices;

    @FXML
    public void initialize() {
        System.out.println("Services View Loaded.");
        fetchServices();
        fetchCities();
        fetchServiceCategories();
        fetchFeedback();
    }

    // Fetch all services
    private void fetchServices() {
        String apiUrl = "http://localhost:8080/api/homeless-services";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        allServices = JsonUtils.getObjectMapper().readValue(response, new TypeReference<List<HomelessService>>() {});
                        displayServices(allServices);
                    } catch (Exception e) {
                        System.err.println("Error processing services JSON response: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    private void updateServiceContainer(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());

            allServices = mapper.readValue(response, new TypeReference<List<HomelessService>>() {});
            displayServices(allServices);
        } catch (Exception e) {
            System.err.println("Error processing services JSON response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayServices(List<HomelessService> services) {
        Platform.runLater(() -> {
            servicesContainer.getChildren().clear();

            for (HomelessService service : services) {
                // Calculate the average rating for this service
                double averageRating = calculateAverageRating(service.serviceId);

                VBox serviceBox = new VBox(5);
                serviceBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");

                // Create the Give Feedback button
                Button feedbackButton = new Button("Give Feedback");
                feedbackButton.setOnAction(event -> openFeedbackDialog(service.serviceId));

                // Add all service details and the button
                serviceBox.getChildren().addAll(
                        new Label("Service ID: " + service.serviceId),
                        new Label("Name: " + service.name),
                        new Label("Address: " + service.address),
                        new Label("Contact: " + service.contactNumber),
                        new Label("Start Time: " + service.startTime),
                        new Label("End Time: " + service.endTime),
                        new Label("City: " + (service.city != null ? service.city.cityName : "N/A")),
                        new Label("Category: " + (service.category != null ? service.category.categoryName : "N/A")),
                        new Label("Average Rating: " + (averageRating > 0 ? String.format("%.2f", averageRating) : "No Ratings")),
                        feedbackButton
                );

                servicesContainer.getChildren().add(serviceBox);
            }
        });
    }

    private void fetchCities() {
        fetchComboBoxData("http://localhost:8080/api/cities", cityComboBox, "cityName");
    }

    private void fetchServiceCategories() {
        fetchComboBoxData("http://localhost:8080/api/service-categories", categoryComboBox, "categoryName");
    }

    private void fetchComboBoxData(String apiUrl, ComboBox<String> comboBox, String fieldName) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                        List<String> items = mapper.readTree(response)
                                .findValuesAsText(fieldName);

                        Platform.runLater(() -> {
                            comboBox.getItems().clear();
                            comboBox.getItems().addAll(items);
                        });
                    } catch (Exception e) {
                        System.err.println("Error processing JSON for " + apiUrl + ": " + e.getMessage());
                    }
                });
    }

    // Apply filters for city, category, and time range
    @FXML
    private void applyFilters() {
        String selectedCity = cityComboBox.getValue();
        String selectedCategory = categoryComboBox.getValue();
        String startTimeInput = startTimeField.getText();
        String endTimeInput = endTimeField.getText();
        boolean isOpenedNow = openedNowCheckBox.isSelected();

        System.out.println("Selected City: " + selectedCity);
        System.out.println("Selected Category: " + selectedCategory);
        System.out.println("Start Time Input: " + startTimeInput);
        System.out.println("End Time Input: " + endTimeInput);
        System.out.println("Opened Now: " + isOpenedNow);

        // Declare startTime and endTime as final
        final LocalTime startTime = (startTimeInput != null && !startTimeInput.trim().isEmpty())
                ? LocalTime.parse(startTimeInput) : null;
        final LocalTime endTime = (endTimeInput != null && !endTimeInput.trim().isEmpty())
                ? LocalTime.parse(endTimeInput) : null;

        // Declare current time as final
        final LocalTime currentTime = LocalTime.now();

        List<HomelessService> filteredServices = allServices.stream()
                .filter(service -> selectedCity == null || selectedCity.isEmpty() ||
                        (service.city != null && service.city.cityName.equals(selectedCity)))
                .filter(service -> selectedCategory == null || selectedCategory.isEmpty() ||
                        (service.category != null && service.category.categoryName.equals(selectedCategory)))
                .filter(service -> {
                    if (startTime != null) {
                        LocalTime serviceStartTime = LocalTime.parse(service.startTime);
                        return !serviceStartTime.isBefore(startTime);
                    }
                    return true; // If no start time is specified
                })
                .filter(service -> {
                    if (endTime != null) {
                        LocalTime serviceEndTime = LocalTime.parse(service.endTime);
                        return !serviceEndTime.isAfter(endTime);
                    }
                    return true; // If no end time is specified
                })
                .filter(service -> !isOpenedNow ||
                        (LocalTime.parse(service.startTime).isBefore(currentTime) &&
                                LocalTime.parse(service.endTime).isAfter(currentTime)))
                .collect(Collectors.toList());

        System.out.println("Filtered Services: " + filteredServices.size());
        displayServices(filteredServices);
    }


    @FXML
    private void clearFilters() {
        cityComboBox.setValue(null);
        categoryComboBox.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        openedNowCheckBox.setSelected(false); // Reset "Opened Now" checkbox
        displayServices(allServices);
    }

    private void fetchFeedback() {
        String feedbackApiUrl = "http://localhost:8080/api/feedbacks";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(feedbackApiUrl)).build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        feedbackList = JsonUtils.getObjectMapper().readValue(response, new TypeReference<List<Feedback>>() {});
                        System.out.println("Feedback fetched: " + feedbackList.size());
                        displayServices(allServices);
                    } catch (Exception e) {
                        System.err.println("Error processing feedback JSON response: " + e.getMessage());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error fetching feedback: " + e.getMessage());
                    return null;
                });
    }

    private double calculateAverageRating(Long serviceId) {
        if (feedbackList == null || feedbackList.isEmpty()) {
            return 0.0;
        }

        // Filter feedback for the given serviceId and calculate the average
        List<Integer> ratings = feedbackList.stream()
                .filter(f -> f.getService() != null && f.getService().getServiceId().equals(serviceId))
                .map(Feedback::getRating)
                .collect(Collectors.toList());

        if (ratings.isEmpty()) {
            return 0.0;
        }

        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private void openFeedbackDialog(Long serviceId) {
        // Create the dialog
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Give Feedback");
        dialog.setHeaderText("Submit your feedback for Service ID: " + serviceId);

        // Input for rating
        Label ratingLabel = new Label("Rating (1-5):");
        TextField ratingField = new TextField();

        // Layout for dialog content
        VBox content = new VBox(10);
        content.getChildren().addAll(ratingLabel, ratingField);
        dialog.getDialogPane().setContent(content);

        // Add OK and Cancel buttons
        ButtonType submitButton = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

        // Process the result
        dialog.setResultConverter(button -> {
            if (button == submitButton) {
                try {
                    return Integer.parseInt(ratingField.getText().trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid rating input. Please enter a number between 1 and 5.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(rating -> {
            if (rating >= 1 && rating <= 5) {
                sendFeedback(serviceId, rating);
            } else {
                System.err.println("Rating must be between 1 and 5.");
            }
        });
    }

    private void sendFeedback(Long serviceId, int rating) {
        Long userId = getCurrentUserId();

        if (userId == null) {
            return; // Exit method if user is not logged in
        }

        // Construct the feedback JSON
        String feedbackJson = String.format(
                "{\"rating\": %d, \"user\": {\"userId\": %d}, \"service\": {\"serviceId\": %d}}",
                rating, userId, serviceId
        );
        System.out.println("Feedback JSON: " + feedbackJson); // Debugging log

        String apiUrl = "http://localhost:8080/api/feedbacks";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(feedbackJson))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    if (statusCode == 201 || statusCode == 200) {
                        showAlert("Feedback Submitted", "Thank you for your feedback!");
                        fetchFeedback(); // Refresh feedback data
                    } else {
                        showAlert("Submission Failed", "Failed to submit feedback. Please try again.");
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error submitting feedback: " + e.getMessage());
                    showAlert("Error", "An error occurred while submitting feedback.");
                    return null;
                });
    }

    private Long getCurrentUserId() {
        Long userId = UserSession.getInstance().getCurrentUserId();
        if (userId == null) {
            showAlert("Error", "You must be logged in to give feedback. Please log in and try again.");
            return null;
        }
        return userId;
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomelessService {
        public Long serviceId;
        public String name;
        public String address;
        public String contactNumber;
        public String startTime;
        public String endTime;
        public City city;
        public Category category;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class City {
            public String cityName;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Category {
            public String categoryName;
        }
    }
}
