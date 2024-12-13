package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class ManageCitiesMenuController {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @FXML
    private void handleGetAllCities() {
        try {
            URL url = new URL(BASE_API_URL + "/cities");
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
                    showAlert("All Cities", response.toString());
                }
            } else {
                showAlert("Error", "Failed to fetch cities. Response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleGetCityById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get City");
        dialog.setHeaderText("Enter City ID:");
        dialog.setContentText("City ID:");

        Optional<String> cityId = dialog.showAndWait();
        cityId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/cities/" + id);
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
                        showAlert("City Details", response.toString());
                    }
                } else {
                    showAlert("Error", "Failed to fetch city. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleCreateCity() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Create City");
        nameDialog.setHeaderText("Enter city name:");
        nameDialog.setContentText("City Name:");

        Optional<String> cityName = nameDialog.showAndWait();
        if (cityName.isPresent()) {
            try {
                String payload = String.format("{\"cityName\":\"%s\"}", cityName.get());

                URL url = new URL(BASE_API_URL + "/cities");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payload.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    showAlert("Success", "City created successfully!");
                } else {
                    showAlert("Error", "Failed to create city. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteCityById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete City");
        dialog.setHeaderText("Enter City ID:");
        dialog.setContentText("City ID:");

        Optional<String> cityId = dialog.showAndWait();
        cityId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/cities/" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    showAlert("Success", "City deleted successfully!");
                } else {
                    showAlert("Error", "Failed to delete city. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Admin Menu");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to go back to Admin Menu.");
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
