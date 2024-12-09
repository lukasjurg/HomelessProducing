package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import javafx.stage.Stage;
import org.json.JSONObject;


public class MainViewController {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @FXML
    private void handleLogin() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Login");
        usernameDialog.setHeaderText("Enter your username");
        usernameDialog.setContentText("Username:");

        Optional<String> username = usernameDialog.showAndWait();
        if (username.isPresent()) {
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Login");
            passwordDialog.setHeaderText("Enter your password");
            passwordDialog.setContentText("Password:");

            Optional<String> password = passwordDialog.showAndWait();
            if (password.isPresent()) {
                try {
                    String userRole = authenticateUser(username.get(), password.get());

                    if ("User".equalsIgnoreCase(userRole)) {
                        openUserMenu();
                    } else if ("Admin".equalsIgnoreCase(userRole)) {
                        openAdminMenu();
                    } else {
                        showAlert("Login Failed", "Invalid credentials or role!");
                    }
                } catch (IOException e) {
                    showAlert("Error", "Failed to connect to the server!");
                    e.printStackTrace();
                }
            }
        }
    }

    private void openUserMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open User Menu.");
        }
    }

    private void openAdminMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Admin Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Admin Menu.");
        }
    }

    @FXML
    private void handleRegister() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Register");
        usernameDialog.setHeaderText("Enter your username for registration");
        usernameDialog.setContentText("Username:");

        Optional<String> username = usernameDialog.showAndWait();
        if (username.isPresent()) {
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Register");
            passwordDialog.setHeaderText("Enter your password for registration");
            passwordDialog.setContentText("Password:");

            Optional<String> password = passwordDialog.showAndWait();
            if (password.isPresent()) {
                try {
                    boolean isRegistered = registerUser(username.get(), password.get());

                    if (isRegistered) {
                        showAlert("Registration Success", "User registered successfully!");
                    } else {
                        showAlert("Registration Failed", "Registration failed. Try again!");
                    }
                } catch (IOException e) {
                    showAlert("Error", "Failed to connect to the server!");
                    e.printStackTrace();
                }
            }
        }
    }


    private String authenticateUser(String username, String password) throws IOException {
        URL url = new URL(BASE_API_URL + "/users/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String payload = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Login Response: " + response);

                String responseString = response.toString().trim();
                if (responseString.startsWith("{")) {
                    JSONObject json = new JSONObject(responseString);
                    return json.optString("role", null);
                } else {
                    if (responseString.equalsIgnoreCase("Login successful!")) {
                        return "User";
                    }
                }
            }
        } else {
            System.out.println("Error Response Code: " + responseCode);
        }
        return null;
    }

    private boolean registerUser(String username, String password) throws IOException {
        URL url = new URL(BASE_API_URL + "/users/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String payload = String.format("{\"username\":\"%s\", \"email\":\"%s@example.com\", \"password\":\"%s\"}",
                username, username, password);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Registration Response: " + response);

                if (response.toString().trim().equals("User registered successfully!")) {
                    return true;
                } else {
                    showAlert("Registration Failed", response.toString());
                }
            }
        } else {
            System.out.println("Error Response Code: " + responseCode);
            showAlert("Registration Failed", "Server error occurred!");
        }
        return false; // Registration failed
    }




    private String parseUserRole(String jsonResponse) {
        if (jsonResponse.contains("\"role\":\"User\"")) {
            return "User";
        } else if (jsonResponse.contains("\"role\":\"Admin\"")) {
            return "Admin";
        }
        return null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
