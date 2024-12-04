package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import team15.homelessproducing.service.UserService;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UserService userService;

    public LoginController() {
        this.userService = new UserService(); // Connect to back-end
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (userService.validateLogin(email, password)) {
            // Navigate to user/admin dashboard
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Login Successful!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid credentials!");
            alert.showAndWait();
        }
    }
}
