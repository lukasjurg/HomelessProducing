package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

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
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}