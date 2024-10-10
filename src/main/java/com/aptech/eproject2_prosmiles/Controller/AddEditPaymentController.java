package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import javafx.collections.ObservableList;

public class AddEditPaymentController {
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

}
