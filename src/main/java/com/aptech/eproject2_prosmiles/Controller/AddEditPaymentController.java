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
    private Button btn_cancel;
    @FXML
    private Button btn_save;
    @FXML
    private ComboBox<EPaymentType> cmb_payment_type;
    @FXML
    private ComboBox<EStatus> cmb_status;
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
        cmb_status.getItems().clear();
        for (EStatus status : EStatus.values()) {
            cmb_status.getItems().add(status);
        }

        cmb_payment_type.getItems().clear();
        for (EPaymentType paymentType : EPaymentType.values()) {
            cmb_payment_type.getItems().add(paymentType);
        }

        btn_save.setOnAction(this::handleSave);
        btn_cancel.setOnAction(event -> dialogStage.close());
    }


    public void initializeForm(){
        if(payment != null){
            System.out.println(payment);
            lbl_patient_name.setText(payment.getPrescription().getPatient().getName());
            lbl_payment_number.setText(payment.getBillNumber());
            lbl_amount.setText(String.valueOf(payment.getTotalAmount()));
            cmb_payment_type.setValue(payment.getPaymentType());
            cmb_status.setValue(payment.getPrescription().getStatus());
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        try{
            if(cmb_payment_type.getSelectionModel().getSelectedItem() == null){
                throw new Exception("Payment type is not selected");
            }
            payment.setPaymentType(cmb_payment_type.getSelectionModel().getSelectedItem());
            if(cmb_status.getSelectionModel().getSelectedItem() == null){
                throw new Exception("Status is not selected");
            }
            payment.getPrescription().setStatus(cmb_status.getSelectionModel().getSelectedItem());

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
