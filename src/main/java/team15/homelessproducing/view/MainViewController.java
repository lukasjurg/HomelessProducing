package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainViewController {

    @FXML
    private void handleLogin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText("Login button clicked!");
        alert.showAndWait();
    }

    @FXML
    private void handleRegister() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText("Register button clicked!");
        alert.showAndWait();
    }
}
