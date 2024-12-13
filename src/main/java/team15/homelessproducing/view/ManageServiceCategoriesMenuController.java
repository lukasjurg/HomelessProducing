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

public class ManageServiceCategoriesMenuController {

    private static final String BASE_API_URL = "http://localhost:8080/api/service-categories";

    @FXML
    private void handleGetAllServiceCategories() {
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
                    showAlert("All Service Categories", response.toString());
                }
            } else {
                showAlert("Error", "Failed to fetch service categories. Response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleGetServiceCategoryById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Service Category");
        dialog.setHeaderText("Enter Service Category ID:");
        dialog.setContentText("Category ID:");

        Optional<String> categoryId = dialog.showAndWait();
        categoryId.ifPresent(id -> {
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
                        showAlert("Service Category Details", response.toString());
                    }
                } else {
                    showAlert("Error", "Failed to fetch service category. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleCreateServiceCategory() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Create Service Category");
        nameDialog.setHeaderText("Enter category name:");
        nameDialog.setContentText("Name:");

        Optional<String> name = nameDialog.showAndWait();

        TextInputDialog descriptionDialog = new TextInputDialog();
        descriptionDialog.setTitle("Create Service Category");
        descriptionDialog.setHeaderText("Enter category description:");
        descriptionDialog.setContentText("Description:");

        Optional<String> description = descriptionDialog.showAndWait();

        if (name.isPresent() && description.isPresent()) {
            try {
                String payload = String.format("{\"categoryName\":\"%s\", \"categoryDescription\":\"%s\"}",
                        name.get(), description.get());

                URL url = new URL(BASE_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payload.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    showAlert("Success", "Service category created successfully!");
                } else {
                    showAlert("Error", "Failed to create service category. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteServiceCategoryById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Service Category");
        dialog.setHeaderText("Enter Service Category ID:");
        dialog.setContentText("Category ID:");

        Optional<String> categoryId = dialog.showAndWait();
        categoryId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    showAlert("Success", "Service category deleted successfully!");
                } else {
                    showAlert("Error", "Failed to delete service category. Response code: " + responseCode);
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
            showAlert("Error", "Failed to load Admin Menu.");
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
