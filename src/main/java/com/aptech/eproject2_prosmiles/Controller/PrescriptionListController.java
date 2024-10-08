package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class PrescriptionListController {

    @FXML
    private TableView<Prescription> tablePrescription;

    @FXML
    private TableColumn<Prescription, Integer> colId;
    @FXML
    private TableColumn<Prescription, String> colPatientName;
    @FXML
    private TableColumn<Prescription, String> colDescription;
    @FXML
    private TableColumn<Prescription, String> colServiceName;
    @FXML
    private TableColumn<Prescription, String> colCreatedAt;
    @FXML
    private TableColumn<Prescription, String> colStatus;

    @FXML
    private Button btnAddNew;

    private static ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();
    private static ObservableList<PrescriptionDetail> prescriptionDetails = FXCollections.observableArrayList();

    private final PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();

    @FXML
    public void initialize() {
        // Load the data only if the list is empty
        if (prescriptions.isEmpty()) {
            loadPrescriptionData();
        }
        setupTableColumns();
        tablePrescription.setItems(prescriptions);

        // Add a listener to handle row clicks
        tablePrescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showPrescriptionDetail(newValue);
            }
        });

        btnAddNew.setOnAction(event -> openAddNewPrescriptionForm());
    }

    private void loadPrescriptionData() {
        prescriptions.clear();
        prescriptionDetails.clear();
        prescriptions.addAll(prescriptionDAO.getAll());
        prescriptionDetails.addAll(prescriptionDetailDAO.getAll());
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientName.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            Patient patient = patientDAO.getById(prescription.getPatient().getId());
            return new SimpleStringProperty(patient.getName());
        });

        colDescription.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getDescription());
        });

        colServiceName.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            PrescriptionDetail prescriptionDetail = prescriptionDetailDAO.getById(prescription.getId());
            Service service = serviceDAO.getById(prescriptionDetail.getService().getId());
            return new SimpleStringProperty(service.getName());
        });

        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colStatus.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getStatus().getStatus());
        });
    }

    public void refreshPrescriptionList() {
        prescriptions.clear();
        prescriptionDetails.clear();
        loadPrescriptionData();
        tablePrescription.refresh();
    }

    private void showPrescriptionDetail(Prescription selectedPrescription) {
        try {
            // Load the prescription detail view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/PrescriptionDetail.fxml"));
            Parent prescriptionDetailView = loader.load();

            // Get the controller for the detail view and pass the selected prescription to it
            PrescriptionDetailController detailController = loader.getController();
            detailController.setPrescriptionDetail(selectedPrescription);

            // Display the prescription detail view in a new window or in a dialog
            Stage detailStage = new Stage();
            detailStage.setTitle("Prescription Detail");
            detailStage.setScene(new Scene(prescriptionDetailView));
            detailStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAddNewPrescriptionForm() {
        try {
            // Load the Add New Prescription form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/AddEditPrescription.fxml"));
            Parent addNewPrescriptionView = loader.load();

            // Get the controller for the add form and pass a reference of this PrescriptionListController
            AddEditPrescriptionController addEditPrescriptionController = loader.getController();
            addEditPrescriptionController.setPrescriptionListController(this);

            // Create a new stage for the form
            Stage addNewStage = new Stage();
            addNewStage.setTitle("Add New Prescription");
            addNewStage.setScene(new Scene(addNewPrescriptionView));
            addNewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
