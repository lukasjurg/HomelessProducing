package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class ManageUsersMenuController {

    private static final String BASE_API_URL = "http://localhost:8080/api/users";

    @FXML
    private void handleGetAllUsers() {
        try {
            URL url = new URL(BASE_API_URL);
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
                    showAlert("All Users", response.toString());
                }
            } else {
                showAlert("Error", "Failed to fetch users. Response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleGetUserById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get User");
        dialog.setHeaderText("Enter User ID:");
        dialog.setContentText("User ID:");

        Optional<String> userId = dialog.showAndWait();
        userId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        showAlert("User Details", response.toString());
                    }
                } else {
                    showAlert("Error", "Failed to fetch user. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleUpdateUserById() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update User");
        idDialog.setHeaderText("Enter User ID to Update:");
        idDialog.setContentText("User ID:");

        Optional<String> userId = idDialog.showAndWait();

        if (userId.isPresent()) {
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Update User");
            usernameDialog.setHeaderText("Enter the new username:");
            usernameDialog.setContentText("Username:");
            Optional<String> newUsername = usernameDialog.showAndWait();

            TextInputDialog emailDialog = new TextInputDialog();
            emailDialog.setTitle("Update User");
            emailDialog.setHeaderText("Enter the new email:");
            emailDialog.setContentText("Email:");
            Optional<String> newEmail = emailDialog.showAndWait();

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Update User");
            passwordDialog.setHeaderText("Enter the new password:");
            passwordDialog.setContentText("Password:");
            Optional<String> newPassword = passwordDialog.showAndWait();

            TextInputDialog roleDialog = new TextInputDialog();
            roleDialog.setTitle("Update User");
            roleDialog.setHeaderText("Enter the new role ID:");
            roleDialog.setContentText("Role ID (1 for User, 2 for Admin):");
            Optional<String> roleId = roleDialog.showAndWait();

            if (newUsername.isPresent() && newEmail.isPresent() && newPassword.isPresent() && roleId.isPresent()) {
                try {
                    String payload = String.format(
                            "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"role\":{\"roleId\":%s}}",
                            newUsername.get(), newEmail.get(), newPassword.get(), roleId.get()
                    );

                    URL url = new URL(BASE_API_URL + "/" + userId.get());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(payload.getBytes());
                        os.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        showAlert("Success", "User updated successfully!");
                    } else {
                        showAlert("Error", "Failed to update user. Response code: " + responseCode);
                    }
                } catch (Exception e) {
                    showAlert("Error", "An error occurred: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleBackToAdminMenu(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenu.fxml"));
            Scene adminMenuScene = new Scene(loader.load());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(adminMenuScene);
            window.setTitle("Admin Menu");
            window.show();
        } catch (Exception e) {
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
