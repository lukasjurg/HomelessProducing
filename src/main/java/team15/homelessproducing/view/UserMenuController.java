package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMenuController {

    @FXML
    private void handleManageUsers() {
        try {
            // Load the Manage Users Menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ManageUsersMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Users");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to open Manage Users Menu.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        showAlert("Info", "User logged out!");
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
