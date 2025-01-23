package team15.homelessproducing.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
public class ManageServicesMenuController {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @FXML
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
                        response.append(line);
                    }

                    String formattedResponse = formatJson(response.toString());
                    displayFormattedText("All Services", formattedResponse);
                }
            } else {
                showAlert("Error", "Failed to fetch services. Response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleGetServiceById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Service");
        dialog.setHeaderText("Enter Service ID:");
        dialog.setContentText("Service ID:");

        Optional<String> serviceId = dialog.showAndWait();
        serviceId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/homeless-services/" + id);
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

                        String formattedResponse = formatJson(response.toString());
                        displayFormattedText("Service Details", formattedResponse);
                    }
                } else {
                    showAlert("Error", "Failed to fetch service. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleCreateService() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Create Service");
        nameDialog.setHeaderText("Enter service name:");
        nameDialog.setContentText("Name:");
        Optional<String> name = nameDialog.showAndWait();

        TextInputDialog addressDialog = new TextInputDialog();
        addressDialog.setTitle("Create Service");
        addressDialog.setHeaderText("Enter service address:");
        addressDialog.setContentText("Address:");
        Optional<String> address = addressDialog.showAndWait();

        TextInputDialog contactDialog = new TextInputDialog();
        contactDialog.setTitle("Create Service");
        contactDialog.setHeaderText("Enter contact number:");
        contactDialog.setContentText("Contact Number:");
        Optional<String> contactNumber = contactDialog.showAndWait();

        TextInputDialog startTimeDialog = new TextInputDialog();
        startTimeDialog.setTitle("Create Service");
        startTimeDialog.setHeaderText("Enter start time (HH:mm:ss):");
        startTimeDialog.setContentText("Start Time:");
        Optional<String> startTime = startTimeDialog.showAndWait();

        TextInputDialog endTimeDialog = new TextInputDialog();
        endTimeDialog.setTitle("Create Service");
        endTimeDialog.setHeaderText("Enter end time (HH:mm:ss):");
        endTimeDialog.setContentText("End Time:");
        Optional<String> endTime = endTimeDialog.showAndWait();

        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Create Service");
        categoryDialog.setHeaderText("Enter category ID:");
        categoryDialog.setContentText("Category ID:");
        Optional<String> categoryId = categoryDialog.showAndWait();

        TextInputDialog cityDialog = new TextInputDialog();
        cityDialog.setTitle("Create Service");
        cityDialog.setHeaderText("Enter city ID:");
        cityDialog.setContentText("City ID:");
        Optional<String> cityId = cityDialog.showAndWait();

        if (name.isPresent() && address.isPresent() && contactNumber.isPresent() &&
                startTime.isPresent() && endTime.isPresent() &&
                categoryId.isPresent() && cityId.isPresent()) {
            try {
                // Construct the JSON payload
                String payload = String.format(
                        "{\"name\":\"%s\", \"address\":\"%s\", \"contactNumber\":\"%s\", \"startTime\":\"%s\", \"endTime\":\"%s\", \"category\":{\"categoryId\":%s}, \"city\":{\"cityId\":%s}}",
                        name.get(), address.get(), contactNumber.get(), startTime.get(), endTime.get(), categoryId.get(), cityId.get()
                );

                URL url = new URL(BASE_API_URL + "/homeless-services");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payload.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showAlert("Success", "Service created successfully!");
                } else {
                    showAlert("Error", "Failed to create service. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateServiceById() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Service");
        idDialog.setHeaderText("Enter Service ID to Update:");
        idDialog.setContentText("Service ID:");

        Optional<String> serviceId = idDialog.showAndWait();

        if (serviceId.isPresent()) {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Update Service");
            nameDialog.setHeaderText("Enter the new service name:");
            nameDialog.setContentText("Name:");
            Optional<String> name = nameDialog.showAndWait();

            TextInputDialog addressDialog = new TextInputDialog();
            addressDialog.setTitle("Update Service");
            addressDialog.setHeaderText("Enter the new address:");
            addressDialog.setContentText("Address:");
            Optional<String> address = addressDialog.showAndWait();

            TextInputDialog contactDialog = new TextInputDialog();
            contactDialog.setTitle("Update Service");
            contactDialog.setHeaderText("Enter the new contact number:");
            contactDialog.setContentText("Contact Number:");
            Optional<String> contactNumber = contactDialog.showAndWait();

            TextInputDialog startTimeDialog = new TextInputDialog();
            startTimeDialog.setTitle("Update Service");
            startTimeDialog.setHeaderText("Enter the new start time (HH:mm:ss):");
            startTimeDialog.setContentText("Start Time:");
            Optional<String> startTime = startTimeDialog.showAndWait();

            TextInputDialog endTimeDialog = new TextInputDialog();
            endTimeDialog.setTitle("Update Service");
            endTimeDialog.setHeaderText("Enter the new end time (HH:mm:ss):");
            endTimeDialog.setContentText("End Time:");
            Optional<String> endTime = endTimeDialog.showAndWait();

            TextInputDialog categoryDialog = new TextInputDialog();
            categoryDialog.setTitle("Update Service");
            categoryDialog.setHeaderText("Enter the new category ID:");
            categoryDialog.setContentText("Category ID:");
            Optional<String> categoryId = categoryDialog.showAndWait();

            TextInputDialog cityDialog = new TextInputDialog();
            cityDialog.setTitle("Update Service");
            cityDialog.setHeaderText("Enter the new city ID:");
            cityDialog.setContentText("City ID:");
            Optional<String> cityId = cityDialog.showAndWait();

            if (name.isPresent() && address.isPresent() && contactNumber.isPresent() &&
                    startTime.isPresent() && endTime.isPresent() &&
                    categoryId.isPresent() && cityId.isPresent()) {
                try {
                    String payload = String.format(
                            "{\"name\":\"%s\", \"address\":\"%s\", \"contactNumber\":\"%s\", \"startTime\":\"%s\", \"endTime\":\"%s\", \"category\":{\"categoryId\":%s}, \"city\":{\"cityId\":%s}}",
                            name.get(), address.get(), contactNumber.get(), startTime.get(), endTime.get(), categoryId.get(), cityId.get()
                    );

                    URL url = new URL(BASE_API_URL + "/homeless-services/" + serviceId.get());
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
                        showAlert("Success", "Service updated successfully!");
                    } else {
                        showAlert("Error", "Failed to update service. Response code: " + responseCode);
                    }
                } catch (Exception e) {
                    showAlert("Error", "An error occurred: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
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

    private String formatJson(String jsonString) {
        try {
            if (jsonString.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonString);
                return jsonArray.toString(4);
            } else {
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.toString(4);
            }
        } catch (Exception e) {
            return jsonString;
        }
    }

    private void displayFormattedText(String title, String message) {
        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
