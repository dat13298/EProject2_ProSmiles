package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddEditPrescriptionController extends BaseController implements Initializable {


    @FXML
    private Button btnSave;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPatientId;


    @FXML
    private ComboBox<Staff> comboStaff;

    @FXML
    private ComboBox<EStatus> comboStatus;


    @FXML
    private Button btnCancel;

    @FXML
    private Label lbl_inform;

    private Stage dialogStage;
    private boolean isEditMode;
    private boolean saved = false;
    private Prescription prescription;
    PrescriptionDAO prescriptionDAO = new PrescriptionDAO();


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public boolean getIsSaved() {
        System.out.println("Saved: " + saved);
        return saved;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StaffDAO staffDAO = new StaffDAO();
        ObservableList<Staff> staffs = staffDAO.getAll();
        comboStaff.getItems().clear();
        comboStaff.getItems().addAll(staffs);

        comboStatus.getItems().clear();
        for(EStatus status : EStatus.values()) {
            comboStatus.getItems().add(status);
        }

        btnSave.setOnAction(this::handleSave);
        btnCancel.setOnAction(event -> dialogStage.close());
    }

    public void initializeForm() {
        if(prescription != null) {
            if (prescription.getPatient() != null) {
                txtPatientId.setText(String.valueOf(prescription.getPatient().getId()));
            } else {
                txtPatientId.clear(); // Clear the text field if there is no patient
            }
            txtDescription.setText(prescription.getDescription());
            comboStaff.setValue(prescription.getStaff());
            comboStatus.setValue(prescription.getStatus());
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        PatientDAO patientDAO = new PatientDAO();
        try{
            String patientId = txtPatientId.getText();
            if(patientId == null || patientId.isEmpty()) {
                throw new Exception("Patient ID cannot be empty");
            }
            prescription.setPatient(patientDAO.getById(Integer.parseInt(patientId)));

            String description = txtDescription.getText();
            if(description == null || description.isEmpty()) {
                throw new Exception("Description cannot be empty");
            }
            prescription.setDescription(description);

            Staff staff = comboStaff.getSelectionModel().getSelectedItem();
            if(staff == null) {
                throw new Exception("Staff cannot be empty");
            }
            prescription.setStaff(staff);

            EStatus status = comboStatus.getSelectionModel().getSelectedItem();
            if(status == null) {
                throw new Exception("Status cannot be empty");
            }
            prescription.setStatus(status);

            if(isEditMode){
                DialogHelper.showNotificationDialog("Edit Success", "Prescription updated successfully");
            }else{
                prescriptionDAO.save(prescription);
                DialogHelper.showNotificationDialog("Add Success", "Prescription added successfully");
            }

            saved = true;
            dialogStage.close();
        }catch(Exception e){
            e.printStackTrace();
            lbl_inform.setText(e.getMessage());
            lbl_inform.setStyle("-fx-text-fill: red;");
        }
    }


}
