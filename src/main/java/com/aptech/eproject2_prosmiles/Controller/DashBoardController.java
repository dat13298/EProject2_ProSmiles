package com.aptech.eproject2_prosmiles.Controller;


import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    private Button btn_log_out;
    @FXML
    private Button btn_home;
    @FXML
    private Rectangle btn_home_underline;

    @FXML
    private Button btn_prescription;
    @FXML
    private Rectangle btn_prescription_underline;

    @FXML
    private Button btn_patient;
    @FXML
    private Rectangle btn_patient_underline;

    @FXML
    private Button btn_service;
    @FXML
    private Rectangle btn_service_underline;

    @FXML
    private Button btn_payment;
    @FXML
    private Rectangle btn_payment_underline;

    @FXML
    private Button btn_staff;
    @FXML
    private Rectangle btn_staff_underline;

    @FXML
    private MenuButton btn_report_overview;
    @FXML
    private Rectangle btn_report_overview_underline;
    @FXML private StackPane stackPaneContent;


    public void setContent(Parent newContentPane){
        stackPaneContent.getChildren().clear();
        stackPaneContent.getChildren().add(newContentPane);
    }

    public void getContentPane(String fileFXML) {
        Parent newContentPane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fileFXML));
            newContentPane = loader.load();
            setContent(newContentPane);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUnderlineEffect(btn_home, btn_home_underline);
        setupUnderlineEffect(btn_prescription, btn_prescription_underline);
        setupUnderlineEffect(btn_patient, btn_patient_underline);
        setupUnderlineEffect(btn_service, btn_service_underline);
        setupUnderlineEffect(btn_payment, btn_payment_underline);
        setupUnderlineEffect(btn_staff, btn_staff_underline);
//        setupUnderlineEffect(btn_report, btn_report_underline);

//        log out
        btn_log_out.setOnAction((ActionEvent event) -> {
            AuthenticationService.logout();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/View/LoginForm.fxml")
            );
            Platform.runLater(() -> {
                Stage stage = (Stage) btn_log_out.getScene().getWindow(); // Get current stage
                stage.close(); // Close current stage
            });
            try {
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Login Form");
                stage.show();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

//        Service
        btn_service.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getContentPane("/com/aptech/eproject2_prosmiles/View/Service/ServiceList.fxml");
            }
        }));

    }

    private void setupUnderlineEffect(Button button, Rectangle underline) {
        button.setOnMouseEntered(e -> {
            underline.setWidth(button.getWidth());
        });
        button.setOnMouseExited(e -> {
            underline.setWidth(0);
        });

    }
}
