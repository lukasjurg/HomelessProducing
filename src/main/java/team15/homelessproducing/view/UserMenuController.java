package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import team15.homelessproducing.util.UserSession;

public class UserMenuController {

    @FXML
    private ImageView logoImageView;

    @FXML
    private ImageView photoImageView;

    @FXML
    private Label welcomeLabel;

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
    private void handleProfile() {
        showAlert("Info", "Navigating to Profile.");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
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
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/fxml/images/HA_logo.png"));
            logoImageView.setImage(logoImage);

            Image photoImage = new Image(getClass().getResourceAsStream("/fxml/images/placeholder.png"));
            photoImageView.setImage(photoImage);

            String username = UserSession.getInstance().getCurrentUsername();
            System.out.println("Initializing UserMenuController with username: " + username);

            welcomeLabel.setText("Welcome, " + (username != null && !username.isEmpty() ? username : "Guest") + "!");
        } catch (Exception e) {
            System.err.println("Failed to load resources or update UI: " + e.getMessage());
            e.printStackTrace();
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
