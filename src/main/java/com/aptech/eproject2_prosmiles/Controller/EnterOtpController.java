package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterOtpController {

    @FXML
    private TextField txt_otp;

    @FXML
    private Label lbl_status;

    private String userEmail;

    public void setEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleVerifyOtp() {
        String enteredOtp = txt_otp.getText();

        try {
            boolean isValidOtp = AuthenticationService.verifyOtp(userEmail, enteredOtp);

            if (isValidOtp) {
                AuthenticationService.showChangePasswordModal(userEmail);

                Stage stage = (Stage) txt_otp.getScene().getWindow();
                stage.close();
            } else {
                lbl_status.setText("OTP Code Wrong");
            }
        } catch (Exception e) {
            lbl_status.setText("Error: " + e.getMessage());
        }
    }
}
