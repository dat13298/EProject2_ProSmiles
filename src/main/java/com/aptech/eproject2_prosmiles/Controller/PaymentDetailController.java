package com.aptech.eproject2_prosmiles.Controller;


import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentDetailController extends BaseController{
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnEdit;
    @FXML
    private Label lblAmount;
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblPaymentNumber;
    @FXML
    private Label lblPaymentType;
    @FXML
    private Label lblStatus;

    private Payment payment;
    private Stage dialogStage;
    private PaymentListController paymentListController;

    public void setPaymentListController(PaymentListController paymentListController) {
        this.paymentListController = paymentListController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPaymentDetail(Payment paymentClicked) {
        this.payment = paymentClicked;
        lblPatientName.setText(paymentClicked.getPrescription().getPatient().getName());
        lblPaymentNumber.setText(paymentClicked.getBillNumber());
        lblAmount.setText(String.valueOf(paymentClicked.getTotalAmount()));
        lblPaymentType.setText(paymentClicked.getPaymentType().getValue());
        lblStatus.setText(paymentClicked.getPrescription().getStatus().getStatus());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEdit.setOnAction(event -> {
            paymentListController.showAddEditPayment(payment);
        });
        btnCancel.setOnAction(event -> dialogStage.close());
    }
}
