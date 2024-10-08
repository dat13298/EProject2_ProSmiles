package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class PrescriptionDetailController {

    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblStaffName;

    @FXML
    private Button btnCancel;

    @FXML
    private TableView<ServiceItem> tblServiceItems;
//    @FXML
//    private TableColumn<ServiceItem, String> colServiceName;
//    @FXML
//    private TableColumn<ServiceItem, String> colQuantity;

    public void setPrescriptionDetail(Prescription prescription) {
        PatientDAO patientDAO = new PatientDAO();
        StaffDAO staffDAO = new StaffDAO();

        Patient patient = patientDAO.getById(prescription.getPatient().getId());
        Staff staff = staffDAO.getById(prescription.getStaff().getId());
        // Set the details for the labels using the Prescription object
        System.out.println(prescription);
        lblPatientName.setText(patient.getName());
        lblDescription.setText(prescription.getDescription());
        lblStatus.setText(prescription.getStatus().getStatus());
        lblStaffName.setText(staff.getFirstName() + " " + staff.getLastName());

//        // Populate the table with ServiceItem details related to this prescription
//        tblServiceItems.setItems(prescription.get);
    }

    @FXML
    public void initialize() {
        // Set action for the "Cancel" button
        btnCancel.setOnAction(event -> handleCancel());
    }

    private void handleCancel() {
        // Get the current stage (window) using the button's scene and close it
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }

}
