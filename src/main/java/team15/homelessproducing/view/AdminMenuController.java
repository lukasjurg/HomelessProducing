package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminMenuController {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @FXML
    private void handleManageUsers() {
        loadMenu("/fxml/ManageUsersMenu.fxml", "Manage Users");
    }

    @FXML
    private void handleManageServices() {
        loadMenu("/fxml/ManageServicesMenu.fxml", "Manage Services");
    }

    @FXML
    private void handleManageCities() {
        loadMenu("/fxml/ManageCitiesMenu.fxml", "Manage Cities");
    }

    @FXML
    private void handleManageServiceCategories() {
        loadMenu("/fxml/ManageServiceCategoriesMenu.fxml", "Manage Service Categories");
    }

    private void handleGetAllServices() {
        try {
            URL url = new URL(BASE_API_URL + "/homeless-services");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    showAlert("All Services", response.toString());
                }
            } else {
                showAlert("Error", "Failed to fetch services. Response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void loadMenu(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load " + title + " menu.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        showAlert("Info", "Admin logged out!");
        // Implement navigation back to the login screen
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}