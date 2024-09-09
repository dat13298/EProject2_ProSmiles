package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
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
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField pwd_password;
    @FXML
    private Button btn_sign_in;
    @FXML
    private Label hl_forgot_password;
    @FXML
    private CheckBox cb_remember;
    @FXML
    private Label lbl_authenticate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties prop = new Properties();
        if(AuthenticationService.authenticateFromFile(prop)){
            Platform.runLater(() -> {
                Stage stage = (Stage) btn_sign_in.getScene().getWindow(); // Get current stage
                stage.close(); // Close current stage
            });
            loadMainMenu();
        }

        btn_sign_in.setOnAction(new EventHandler<ActionEvent>() {
            private Staff staff = new Staff();

            /*EVENT LOGIN BUTTON*/
            @Override
            public void handle(ActionEvent event) {
                String username = txt_username.getText();
                String password = pwd_password.getText();

//                check is phoneNumber or email
                if(Validation.isPhoneNumberValid(username)){
                    staff.setPhone(username);
                }
                if(Validation.isEmailValid(username)){
                    staff.setEmail(username);
                }
                staff.setPassword(password);

                try {
                    lbl_authenticate.setText("");//Make sure the string is empty
//                    alert if it empties
                    if (username.isEmpty()) throw new Exception("Username cannot be empty");
                    if (password.isEmpty()) throw new Exception("Password cannot be empty");

//                    check login success
                    if(AuthenticationService.login(staff)){
                        System.out.println("login success");
                    }


                    if ((username.equals("admin") && password.equals("admin"))) {
//                        write file properties
                        AppProperties.setProperty("staff.loggedin", "true");
                        AppProperties.setProperty("staff.username", username);
                        AppProperties.setProperty("staff.userrole", "admin");
                        AppProperties.setProperty("staff.userid", "1");

//                        Is remember
                        AppProperties.setProperty("staff.isremember", cb_remember.isSelected() ? "true" : "false");

                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow(); //get current scene
                        stage.close(); //close login scene
//                        load main menu
                        loadMainMenu();
                    }
                } catch (Exception e) {
                    lbl_authenticate.visibleProperty().set(true);//set visible true
                    lbl_authenticate.setStyle("-fx-text-fill: #f63838");
                    lbl_authenticate.setText("Login" + e.getMessage());//Notification
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
}
