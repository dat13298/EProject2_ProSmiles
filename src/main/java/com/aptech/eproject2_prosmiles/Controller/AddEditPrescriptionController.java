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
    private Button btn_save;

    @FXML
    private TextField txt_description;

    @FXML
    private TextField txt_patient_id;


    @FXML
    private ComboBox<Staff> cmb_staff;

    @FXML
    private ComboBox<EStatus> cmb_status;


    @FXML
    private Button btn_cancel;

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
        cmb_staff.getItems().clear();
        cmb_staff.getItems().addAll(staffs);

        cmb_status.getItems().clear();
        for(EStatus status : EStatus.values()) {
            cmb_status.getItems().add(status);
        }

        btn_save.setOnAction(this::handleSave);
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    public void initializeForm() {
        if(prescription != null) {
            if (prescription.getPatient() != null) {
                txt_patient_id.setText(String.valueOf(prescription.getPatient().getId()));
            } else {
                txt_patient_id.clear(); // Clear the text field if there is no patient
            }
            txt_description.setText(prescription.getDescription());
            cmb_staff.setValue(prescription.getStaff());
            cmb_status.setValue(prescription.getStatus());
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        PatientDAO patientDAO = new PatientDAO();

        try{

            String patientId = txt_patient_id.getText();
            if (patientId == null || patientId.isEmpty()) {
                throw new Exception("Patient ID cannot be empty");
            }

            Optional<Patient> patientOptional = Optional.ofNullable(patientDAO.getById(Integer.parseInt(patientId)));
            if (patientOptional.isEmpty()) {
                throw new Exception("No patient found with the provided ID");
            }

            Patient patient = patientOptional.get();
            prescription.setPatient(patient);

            String description = txt_description.getText();
            if(description == null || description.isEmpty()) {
                throw new Exception("Description cannot be empty");
            }
            prescription.setDescription(description);

            Staff staff = cmb_staff.getSelectionModel().getSelectedItem();
            if(staff == null) {
                throw new Exception("Staff cannot be empty");
            }
            prescription.setStaff(staff);

            EStatus status = cmb_status.getSelectionModel().getSelectedItem();
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
