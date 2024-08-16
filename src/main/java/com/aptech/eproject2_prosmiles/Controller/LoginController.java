package com.aptech.eproject2_prosmiles.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private String alert;
    private String alert1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_sign_in.setOnAction(new EventHandler<ActionEvent>() {

            /*EVENT LOGIN BUTTON*/
            @Override
            public void handle(ActionEvent event) {
                String username = txt_username.getText();
                String password = pwd_password.getText();

//                    alert if it empties
                    alert = username.isEmpty() ? "Username is empty" : "";
                    alert1 = password.isEmpty() ? "Password is empty" : "";
//                    alert if it does not match
                    if(!username.equals("admin") && alert.isEmpty()){
                        alert = "Username does not exist";
                    }
                    if(!password.equals("admin") && alert1.isEmpty()){
                        alert1 = "Password does not match";
                    }
//                    load MainMenu if valid
                    if(username.equals("admin") && password.equals("admin")){
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow(); //get current scene
                        stage.close(); //close login scene

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                                .getResource("/com/aptech/eproject2_prosmiles/View/MainMenu.fxml"));
                        try {
                            Scene scene = new Scene(fxmlLoader.load()); //
                            stage.setTitle("Main Menu");
                            stage.setScene(scene); // set MainMenu scene to stage
                            stage.show(); //display stage
                        }catch (IOException e){
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                System.out.println(alert + alert1);
            }
            /*END EVENT LOGIN BUTTON*/

            /*FORGOT PASSWORD*/
        });
    }
}
