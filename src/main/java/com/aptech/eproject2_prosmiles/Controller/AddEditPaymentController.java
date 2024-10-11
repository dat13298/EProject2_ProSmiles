package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Enum.EPaymentType;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddEditPaymentController extends BaseController{
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<EPaymentType> comboPaymentType;
    @FXML
    private ComboBox<EStatus> comboStatus;
    @FXML
    private Label lbl_amount;
    @FXML
    private Label lbl_patient_name;
    @FXML
    private Label lbl_payment_number;
    @FXML
    private Label lbl_inform;

    private Stage dialogStage;
    private boolean isEditMode;
    private boolean saved = false;
    private Payment payment;

    public boolean getIsSaved() {
        return saved;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboStatus.getItems().clear();
        for (EStatus status : EStatus.values()) {
            comboStatus.getItems().add(status);
        }

        comboPaymentType.getItems().clear();
        for (EPaymentType paymentType : EPaymentType.values()) {
            comboPaymentType.getItems().add(paymentType);
        }

        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(event -> dialogStage.close());
    }


    public void initializeForm(){
        if(payment != null){
            System.out.println(payment);
            lbl_patient_name.setText(payment.getPrescription().getPatient().getName());
            lbl_payment_number.setText(payment.getBillNumber());
            lbl_amount.setText(String.valueOf(payment.getTotalAmount()));
            comboPaymentType.setValue(payment.getPaymentType());
            comboStatus.setValue(payment.getPrescription().getStatus());
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        try{
            if(comboPaymentType.getSelectionModel().getSelectedItem() == null){
                throw new Exception("Payment type is not selected");
            }
            payment.setPaymentType(comboPaymentType.getSelectionModel().getSelectedItem());
            if(comboStatus.getSelectionModel().getSelectedItem() == null){
                throw new Exception("Status is not selected");
            }
            payment.getPrescription().setStatus(comboStatus.getSelectionModel().getSelectedItem());

            DialogHelper.showNotificationDialog("Edit Success", "Payment has been updated successfully");

            saved = true;
            dialogStage.close();
        }catch (Exception e){
            e.printStackTrace();
            lbl_inform.setText(e.getMessage());
            lbl_inform.setStyle("-fx-text-fill: red");
        }
    }


}
