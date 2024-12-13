package team15.homelessproducing.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

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
        List<String> options = Arrays.asList("Get All Users", "Get User By ID", "Update User By ID");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Get All Users", options);
        dialog.setTitle("Manage Users");
        dialog.setHeaderText("Select an action to perform:");
        dialog.setContentText("Choose an action:");

        Optional<String> choice = dialog.showAndWait();
        choice.ifPresent(selectedAction -> {
            switch (selectedAction) {
                case "Get All Users":
                    handleGetAllUsers();
                    break;
                case "Get User By ID":
                    handleGetUserById();
                    break;
                case "Update User By ID":
                    handleUpdateUserById();
                    break;
                default:
                    showAlert("Error", "Invalid action selected!");
            }
        });
    }

    private void handleGetAllUsers() {
        try {
            URL url = new URL(BASE_API_URL + "/users");
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

    private void handleGetUserById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get User");
        dialog.setHeaderText("Enter User ID:");
        dialog.setContentText("User ID:");

        Optional<String> userId = dialog.showAndWait();
        userId.ifPresent(id -> {
            try {
                URL url = new URL(BASE_API_URL + "/users/" + id);
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

                    URL url = new URL(BASE_API_URL + "/users/" + userId.get());
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
    private void handleManageServices() {
        List<String> options = Arrays.asList("Get All Services", "Get Service By ID", "Update Service By ID", "Create a New Service");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Get All Services", options);
        dialog.setTitle("Manage Services");
        dialog.setHeaderText("Select an action to perform:");
        dialog.setContentText("Choose an action:");

        Optional<String> choice = dialog.showAndWait();
        choice.ifPresent(selectedAction -> {
            switch (selectedAction) {
                case "Get All Services":
                    handleGetAllServices();
                    break;
                case "Get Service By ID":
                    handleGetServiceById();
                    break;
                case "Update Service By ID":
                    handleUpdateServiceById();
                    break;
                case "Create a New Service":
                    handleCreateService();
                    break;
                default:
                    showAlert("Error", "Invalid action selected!");
            }
        });
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
                        showAlert("Service Details", response.toString());
                    }
                } else {
                    showAlert("Error", "Failed to fetch service. Response code: " + responseCode);
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        });
    }

    private void handleUpdateServiceById() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Service");
        idDialog.setHeaderText("Enter Service ID to Update:");
        idDialog.setContentText("Service ID:");

        Optional<String> serviceId = idDialog.showAndWait();

        if (serviceId.isPresent()) {
            // Prompt for all fields
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