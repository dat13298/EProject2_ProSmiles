package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentDetailController extends BaseController{
    private PaymentListController paymentListControllerr;

    public void setPaymentListControllerr(PaymentListController paymentListControllerr) {
        this.paymentListControllerr = paymentListControllerr;
    }

    private Payment payment;

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    private ObservableList<Payment> paymentList;
    private ObservableList<Prescription> prescriptionList;

    public void setPrescriptionList(ObservableList<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    public void setPaymentList(ObservableList<Payment> paymentList) {
        this.paymentList = paymentList;
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

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

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



    public void setUpDetail(Payment selectedPayment) {
        payment = selectedPayment;
        prescriptionDAO = new PrescriptionDAO();
        patientDAO = new PatientDAO();

        Prescription prescription = prescriptionDAO.getById(payment.getPrescription().get().getId());
        Patient patient = patientDAO.getById(prescription.getPatient().getId());

        lblPatientName.setText(patient.getName());
        lblPaymentNumber.setText(payment.getBillNumber());
        lblAmount.setText(String.valueOf(payment.getTotalAmount()));
        lblPaymentType.setText(payment.getPaymentType().getValue());
        lblStatus.setText(prescription.getStatus().getStatus());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCancel.setOnAction(e -> dialogStage.close());
        btnEdit.setOnAction(e -> {});
    }
}
