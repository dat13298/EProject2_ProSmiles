package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField txt_email;

    @FXML
    private Label lbl_status;

    @FXML
    private void handleSendEmail() {
        String email = txt_email.getText();

        try {
            if (email.isEmpty()) {
                lbl_status.setText("Please enter a valid email address");
                return;
            }

            AuthenticationService.forgotPassword(email);

            lbl_status.setText("Email Sent");

            showEnterOtpModal(email);

            Stage stage = (Stage) txt_email.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            lbl_status.setText("Error: " + e.getMessage());
        }
    }

    private void showEnterOtpModal(String email) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/EnterOtpModal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Enter OTP");
            stage.setScene(scene);

            EnterOtpController enterOtpController = fxmlLoader.getController();
            enterOtpController.setEmail(email);

            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
