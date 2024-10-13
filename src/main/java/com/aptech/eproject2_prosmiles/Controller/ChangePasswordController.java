package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML
    private PasswordField pwd_new_password;

    @FXML
    private PasswordField pwd_confirm_password;

    private String userEmail;

    public void setEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleChangePassword() {
        String newPassword = pwd_new_password.getText();
        String confirmPassword = pwd_confirm_password.getText();

        if (newPassword.equals(confirmPassword)) {
            try {
                AuthenticationService.changePassword(userEmail, newPassword);

                Stage stage = (Stage) pwd_new_password.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Password does not match");
        }
    }
}
