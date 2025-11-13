package com.bankingsystem.controller;

import com.bankingsystem.dao.CustomerDAO;
import com.bankingsystem.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProfileViewController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField idNumberField;
    @FXML
    private TextField statusField;
    @FXML
    private Button updateButton;

    private CustomerDAO customerDAO;
    private Customer currentCustomer;
    private boolean isStaff;

    public void initialize(Customer customer, boolean isStaff) {
        this.customerDAO = new CustomerDAO();
        this.currentCustomer = customer;
        this.isStaff = isStaff;

        populateProfileData();
        setupControls();
    }

    private void populateProfileData() {
        if (currentCustomer != null) {
            firstNameField.setText(currentCustomer.getFirstName());
            surnameField.setText(currentCustomer.getSurname());
            addressField.setText(currentCustomer.getAddress());
            idNumberField.setText(currentCustomer.getIdNumber());
            statusField.setText(currentCustomer.getStatus());
        }
    }

    private void setupControls() {
        updateButton.setManaged(isStaff);
        updateButton.setVisible(isStaff);
        
        // Only staff can edit the fields
        firstNameField.setEditable(isStaff);
        surnameField.setEditable(isStaff);
        addressField.setEditable(isStaff);
    }

    @FXML
    private void handleUpdateProfile() {
        if (isStaff) {
            // Update logic here
            String firstName = firstNameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();

            currentCustomer.setFirstName(firstName);
            currentCustomer.setSurname(surname);
            currentCustomer.setAddress(address);

            if (customerDAO.updateCustomer(currentCustomer)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer profile updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer profile.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
