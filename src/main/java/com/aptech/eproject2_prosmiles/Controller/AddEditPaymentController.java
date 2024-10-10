package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEditPaymentController extends BaseController{
    private ObservableList<Payment> paymentList;
    private ObservableList<Prescription> prescriptionList;

    public void setPaymentList(ObservableList<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void setPrescriptionList(ObservableList<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    private PaymentDAO paymentDAO;
    private PrescriptionDAO prescriptionDAO;
    private PatientDAO patientDAO;

    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void setPatientDAO(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public void setPrescriptionDAO(PrescriptionDAO prescriptionDAO) {
        this.prescriptionDAO = prescriptionDAO;
    }

    private Payment payment;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private boolean isEditMode;
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    private boolean save;

    public boolean isSaveMode() {
        return save;
    }


    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<?> comboPaymentType;

    @FXML
    private ComboBox<?> comboStatus;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtPaymentNumber;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
