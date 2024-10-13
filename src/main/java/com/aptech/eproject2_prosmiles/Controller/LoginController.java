package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField pwd_password;
    @FXML
    private TextField txt_password_visible;
    @FXML
    private Button btn_toggle_password;
    @FXML
    private Button btn_sign_in;
    @FXML
    private Label hl_forgot_password;
    @FXML
    private CheckBox cb_remember;
    @FXML
    private Label lbl_authenticate;

    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind the text properties so they always hold the same value
        txt_password_visible.textProperty().bindBidirectional(pwd_password.textProperty());

        Properties prop = new Properties();
        try {
            if(AuthenticationService.authenticateFromFile(prop)){
                Platform.runLater(() -> {
                    Stage stage = (Stage) btn_sign_in.getScene().getWindow(); // Get current stage
                    stage.close(); // Close current stage
                });
                loadMainMenu();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        btn_sign_in.setOnAction(new EventHandler<ActionEvent>() {

            /*EVENT LOGIN BUTTON*/
            @Override
            public void handle(ActionEvent event) {
                String username = txt_username.getText();
                String password = pwd_password.getText();

                try {

//                    cloneRegister();

                    Staff staffLogin = new Staff();
                    if(Validation.isEmailValid(username)){
                        staffLogin.setEmail(username);
                        staffLogin.setPassword(password);
                    }
                    if (Validation.isPhoneNumberValid(username)) {
                        staffLogin.setPhone(username);
                        staffLogin.setPassword(password);
                    }

                    lbl_authenticate.setText("");//Make sure the string is empty
//                    alert if it empties
                    if (username.isEmpty()) throw new Exception("Username cannot be empty");
                    if (password.isEmpty()) throw new Exception("Password cannot be empty");

//                    load MainMenu if valid
                    if (AuthenticationService.login(staffLogin)) {
//                        Is remember
                        AppProperties.setProperty("staff.isremember", cb_remember.isSelected() ? "true" : "false");

                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow(); //get current scene
                        stage.close(); //close login scene
//                        load main menu
                        loadMainMenu();
                    } else throw new Exception("Invalid username or password");
                } catch (Exception e) {
                    lbl_authenticate.visibleProperty().set(true);//set visible true
                    lbl_authenticate.setStyle("-fx-text-fill: #f63838");
                    lbl_authenticate.setText(e.getMessage());//Notification
                }
            }
            /*END EVENT LOGIN BUTTON*/

            /*FORGOT PASSWORD*/
        });
    }
    /* LOAD MAIN MENU FXML */
    private void loadMainMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/com/aptech/eproject2_prosmiles/View/DashBoard.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.setScene(scene); // Set MainMenu scene to new stage
            stage.show(); // Display the stage
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Main Menu: " + e.getMessage());
        }
    }
    /* METHOD FOR VISIBLE PASSWORD */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            txt_password_visible.setVisible(true);
            txt_password_visible.setManaged(true);
            pwd_password.setVisible(false);
            pwd_password.setManaged(false);
            btn_toggle_password.setText("Hide");
        } else {
            txt_password_visible.setVisible(false);
            txt_password_visible.setManaged(false);
            pwd_password.setVisible(true);
            pwd_password.setManaged(true);
            btn_toggle_password.setText("Show");
        }
    }

    public static void cloneRegister() {
        Staff staffRegister = new Staff();
        Role roleRegister = new Role();
        roleRegister.setId(1);
        staffRegister.setRole(Optional.of(roleRegister));
        staffRegister.setFirstName("asd");
        staffRegister.setLastName("asd");
        staffRegister.setEGender(EGender.MALE);
        staffRegister.setPhone("0123456789");
        staffRegister.setAddress("285 Doi can");
        staffRegister.setPassword("admin");
        staffRegister.setEmail("admin@aptech.com");
        staffRegister.setAge(20);
        staffRegister.setImagePath("imagePath");
        staffRegister.setCreatedAt(LocalDateTime.now());

        AuthenticationService.register(staffRegister);

    }
}
