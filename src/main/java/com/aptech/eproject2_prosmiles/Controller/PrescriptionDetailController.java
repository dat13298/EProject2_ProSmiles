package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
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
    private Button btnEdit;

    @FXML
    private TableView<PrescriptionDetail> tblServiceItems;

    @FXML
    private TableColumn<PrescriptionDetail, Integer> colId;
    @FXML
    private TableColumn<PrescriptionDetail, String> colServiceName;

    @FXML
    private TableColumn<PrescriptionDetail, String> colUnit;

    @FXML
    private TableColumn<PrescriptionDetail, String> colQuantity;

    @FXML
    private TableColumn<PrescriptionDetail, String> colPrice;

    //ObservableList declaration
    private ObservableList<Prescription> prescriptions;
    private ObservableList<PrescriptionDetail> prescriptionDetails;
    private ObservableList<Patient> patients;
    private ObservableList<Service> services;
    private ObservableList<Staff> staff;
    private ObservableList<ServiceItem> serviceItems;

    public void setPrescriptions(ObservableList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void setPatients(ObservableList<Patient> patients) {
        this.patients = patients;
    }

    public void setServices(ObservableList<Service> services) {
        this.services = services;
    }

    public void setStaff(ObservableList<Staff> staff) {
        this.staff = staff;
    }

    public void setServiceItems(ObservableList<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    //DAO Declaration
    private PrescriptionDetailDAO prescriptionDetailDAO;
    private PatientDAO patientDAO;
    private PrescriptionDAO prescriptionDAO;
    private ServiceDAO serviceDAO;
    private StaffDAO staffDAO;
    private ServiceItemDAO serviceItemDAO;

    private PrescriptionListController prescriptionListController;

    public void setPrescriptionListController(PrescriptionListController prescriptionListController) {
        this.prescriptionListController = prescriptionListController;
    }

    public void setPrescriptionDetailDAO(PrescriptionDetailDAO prescriptionDetailDAO) {
        this.prescriptionDetailDAO = prescriptionDetailDAO;
    }

    public void setPrescriptionDAO(PrescriptionDAO prescriptionDAO) {
        this.prescriptionDAO = prescriptionDAO;
    }

    public void setPatientDAO(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public void setServiceItemDAO(ServiceItemDAO serviceItemDAO) {
        this.serviceItemDAO = serviceItemDAO;
    }

    //Prescription From List
    private Prescription prescription;

    public void setPrescriptionDetails(ObservableList<PrescriptionDetail> prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPrescriptionDetail(Prescription prescription) {
        patientDAO = new PatientDAO();
        prescriptionDetailDAO = new PrescriptionDetailDAO();
        staffDAO = new StaffDAO();

        prescriptionDetails = FXCollections.observableArrayList(
                prescriptionDetailDAO.getAll().stream()
                        .filter(p -> p.getPrescription().getId() == prescription.getId())
                        .toList()
        );


        Patient patient = patientDAO.getById(prescription.getPatient().getId());
        Staff staff = staffDAO.getById(prescription.getStaff().getId());
        // Set the details for the labels using the Prescription object
        lblPatientName.setText(patient.getName());

        String description = prescription.getDescription();
        int maxLength = 20;

        if (description.length() > maxLength) {
            String shortenedDescription = description.substring(0, maxLength) + "...";
            lblDescription.setText(shortenedDescription);
            Tooltip tooltip = new Tooltip(description);
            lblDescription.setTooltip(tooltip);
        } else {
            lblDescription.setText(description);
        }

        lblStatus.setText(prescription.getStatus().getStatus());
        lblStaffName.setText(staff.getFirstName() + " " + staff.getLastName());

        setPrescription(prescription);

        colId.setCellValueFactory(cellData -> {
            Service service = serviceDAO.getById(cellData.getValue().getService().getId());
            return new ReadOnlyObjectWrapper<>(service.getId());
        });

        colServiceName.setCellValueFactory(cellData -> {
            Service service = serviceDAO.getById(cellData.getValue().getService().getId());
            return new SimpleStringProperty(service.getName());
        });

        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        tblServiceItems.setItems(prescriptionDetails);
        tblServiceItems.refresh();
    }


    @FXML
    public void initialize() {
        // Set action for the "Cancel" button
        btnCancel.setOnAction(event -> handleCancel());
        btnEdit.setOnAction(event -> prescriptionListController.addEditPrescriptionForm(prescription, true));
    }

    private void handleCancel() {
        // Get the current stage (window) using the button's scene and close it
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }



}
