package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientDetailController extends BaseController {
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_edit;
    @FXML
    private TableColumn<Prescription, String> col_create_at;
    @FXML
    private TableColumn<Prescription, String> col_description;
    @FXML
    private TableColumn<Prescription, Integer> col_id;
    @FXML
    private Label lbl_address;
    @FXML
    private Label lbl_age;
    @FXML
    private Label lbl_email;
    @FXML
    private Label lbl_gender;
    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_phone;
    @FXML
    private TableView<Prescription> tbl_prescription_history;

    private ObservableList<Prescription> prescriptionList;
    private Patient patient;
    private Stage detailDialogStage;
    private PatientListController patientListController;
    private PrescriptionListController prescriptionListController = new PrescriptionListController();

    public void setPatientListController(PatientListController patientListController) {
        this.patientListController = patientListController;
    }

    public void setDialogStage(Stage detailDialogStage) {
        this.detailDialogStage = detailDialogStage;
    }

    public void setPatient(Patient patientClicked) {
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

        this.patient = patientClicked;
        prescriptionList = prescriptionDAO.getPrescriptionByPatientId(patient.getId());
        lbl_name.setText(patient.getName());
        lbl_gender.setText(patient.getGender().getGender());
        lbl_phone.setText(patient.getPhone());
        lbl_email.setText(patient.getEmail());
        lbl_age.setText(String.valueOf(patient.getAge()));
        lbl_address.setText(patient.getAddress());

        setupTableColumn(prescriptionList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_edit.setOnAction(event -> {
            patientListController.showAddEditForm(patient, true);
        });

        btn_cancel.setOnAction(event -> detailDialogStage.close());
    }

    private void setupTableColumn(ObservableList<Prescription> prescriptionList) {
        PatientDAO patientDAO = new PatientDAO();
        StaffDAO staffDAO = new StaffDAO();

        tbl_prescription_history.setItems(prescriptionList);

        col_id.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleObjectProperty<>(prescription.getId());
        });

        col_description.setCellValueFactory(cellDat -> {
            Prescription prescription = cellDat.getValue();
            return new SimpleObjectProperty<>(prescription.getDescription());
        });

        col_create_at.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getCreatedAt().toString());
        });

        tbl_prescription_history.setItems(prescriptionList);
        tbl_prescription_history.setRowFactory(tv -> {
            TableRow<Prescription> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Prescription prescriptionClicked = row.getItem();
                Patient patientClicked = patientDAO.getById(prescriptionClicked.getId());
                prescriptionClicked.setPatient(patientClicked);
                Staff staff = staffDAO.getById(prescriptionClicked.getStaff().getId());
                prescriptionClicked.setStaff(staff);

                prescriptionListController.showPrescriptionDetail(prescriptionClicked, false);
            });
            return row;
        });
    }
}
