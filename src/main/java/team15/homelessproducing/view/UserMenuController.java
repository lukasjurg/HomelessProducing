package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class UserMenuController {

    @FXML
    private ImageView logoImageView;

    @FXML
    private ImageView photoImageView;

    @FXML
    private void handleCommunity(ActionEvent event) {
        loadPage("/fxml/CommunityView.fxml", "Community Page");
    }

    @FXML
    private void handleServices(ActionEvent event) {
        loadPage("/fxml/ServicesView.fxml", "Services Page");
    }

    private void loadPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load: " + fxmlPath);
        }
    }

    @FXML
    private void handleCommunity() {
        showAlert("Info", "Navigating to Community Section.");
        // Future implementation: Load community-related UI
    }

    @FXML
    private void handleServices() {
        showAlert("Info", "Navigating to Services Section.");
        // Future implementation: Load services-related UI
    }

    @FXML
    private void handleProfile() {
        showAlert("Info", "Navigating to Profile.");
        // Future implementation: Load profile-related UI
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the main menu or login screen after logout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Homeless Producing - Main Menu");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to log out. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Load logo image
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/fxml/images/HA_logo.png"));
            logoImageView.setImage(logoImage);

            // Placeholder photo image (optional)
            Image photoImage = new Image(getClass().getResourceAsStream("/fxml/images/placeholder.png"));
            photoImageView.setImage(photoImage);
        } catch (Exception e) {
            System.err.println("Failed to load images: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
