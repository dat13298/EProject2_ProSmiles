package com.aptech.eproject2_prosmiles.Controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
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




    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setupUnderlineEffect(btn_home, btn_home_underline);
        setupUnderlineEffect(btn_prescription, btn_prescription_underline);
        setupUnderlineEffect(btn_patient, btn_patient_underline);
        setupUnderlineEffect(btn_service, btn_service_underline);
        setupUnderlineEffect(btn_payment, btn_payment_underline);
        setupUnderlineEffect(btn_staff, btn_staff_underline);
//        setupUnderlineEffect(btn_report, btn_report_underline);

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
