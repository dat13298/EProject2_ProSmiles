package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.*;
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
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrescriptionListController extends BaseController{

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

    @FXML
    private Button btnDelete;


    //ObservableList declaration
    private ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();
    private ObservableList<PrescriptionDetail> prescriptionDetails = FXCollections.observableArrayList();
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Service> services = FXCollections.observableArrayList();
    private ObservableList<Staff> staff = FXCollections.observableArrayList();
    private ObservableList<ServiceItem> serviceItems = FXCollections.observableArrayList();

    //DAO Declaration
    private final PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final StaffDAO staffDAO = new StaffDAO();
    private final ServiceItemDAO serviceItemDAO = new ServiceItemDAO();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prescriptions = prescriptionDAO.getAll();
        prescriptionDetails = prescriptionDetailDAO.getAll();
        patients = patientDAO.getAll();
        services = serviceDAO.getAll();
        staff = staffDAO.getAll();
        serviceItems = serviceItemDAO.getAll();

        setupTableColumns();

        tablePrescription.setEditable(true);
        tablePrescription.setItems(prescriptions);

        tablePrescription.setRowFactory(tv -> {
            TableRow<Prescription> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Prescription prescriptionClicked = row.getItem();
                    showPrescriptionDetail(prescriptionClicked);
                }
            });
            return row;
        });

        btnDelete.setOnAction(event -> {
            Prescription selectedPrescription = tablePrescription.getSelectionModel().getSelectedItem();
            PrescriptionDetail selectedPrescriptionDetail = findPrescriptionDetailByPrescriptionId(selectedPrescription.getId());
            System.out.println(selectedPrescriptionDetail);
            if (selectedPrescription != null) {
                boolean confirmed = showConfirmationDialog("Confirm for delete", "Do you want to DELETE this staff?");
                if (confirmed) {
                    selectedPrescription.setIsDeleted(EIsDeleted.INACTIVE);
                    prescriptionDAO.delete(selectedPrescription);//remove from the DB
                    prescriptionDetailDAO.delete(selectedPrescriptionDetail);
                    tablePrescription.getItems().remove(selectedPrescription);//remove from the list
                    tablePrescription.refresh();
                }
            }
        });

        btnAddNew.setOnAction(event -> {
            Prescription prescription = new Prescription();
            addEditPrescriptionForm(prescription, false);
        });

    }


    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    public void refreshTable() {
        tablePrescription.getItems().clear();
        prescriptions = prescriptionDAO.getAll();
        System.out.println("==== refreshing ===");
        prescriptions.forEach(System.out::println);
        tablePrescription.setItems(prescriptions);
        tablePrescription.refresh();
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


    private void showPrescriptionDetail(Prescription selectedPrescription) {
        try {
            // Load the prescription detail view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/PrescriptionDetail.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Prescription Detail");
            // Get the controller for the add form and pass a reference of this PrescriptionListController

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tablePrescription.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));


            // Get the controller for the detail view and pass the selected prescription to it
            PrescriptionDetailController detailController = loader.getController();

            detailController.setPrescriptionListController(this);
            detailController.setPrescriptionDetail(selectedPrescription);
            detailController.setPrescription(selectedPrescription);

            //ObservationList
            detailController.setPrescriptions(prescriptions);
            detailController.setPrescriptionDetails(prescriptionDetails);
            detailController.setPatients(patients);
            detailController.setServices(services);
            detailController.setStaff(staff);
            detailController.setServiceItems(serviceItems);

            //DAO
            detailController.setPrescriptionDetailDAO(prescriptionDetailDAO);
            detailController.setPatientDAO(patientDAO);
            detailController.setPrescriptionDAO(prescriptionDAO);
            detailController.setServiceDAO(serviceDAO);
            detailController.setStaffDAO(staffDAO);
            detailController.setServiceItemDAO(serviceItemDAO);

            detailController.setPrescriptionDetails(prescriptionDetails);

            // Display the prescription detail view in a new window or in a dialog
            detailController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PrescriptionDetail findPrescriptionDetailByPrescriptionId(int id) {
        return prescriptionDetails.stream()
                .filter(p -> p.getPrescription().getId() == id)
                .findFirst().orElse(null);
    }


    public void addEditPrescriptionForm(Prescription prescription,boolean isEditMode) {
        try {
            // Load the Add New Prescription form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/AddEditPrescription.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(isEditMode ? "Add New Prescription" : "Edit Prescription");
            // Get the controller for the add form and pass a reference of this PrescriptionListController

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tablePrescription.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));
            AddEditPrescriptionController addEditPrescriptionController = loader.getController();

            //Set DAO
            addEditPrescriptionController.setPrescriptionDAO(prescriptionDAO);
            addEditPrescriptionController.setServiceDAO(serviceDAO);
            addEditPrescriptionController.setPrescriptionDetailDAO(prescriptionDetailDAO);
            addEditPrescriptionController.setStaffDAO(staffDAO);
            addEditPrescriptionController.setPatientDAO(patientDAO);
            addEditPrescriptionController.setServiceItemDAO(serviceItemDAO);

            //Set ObservableList
            addEditPrescriptionController.setPrescriptions(prescriptions);
            addEditPrescriptionController.setPrescriptionDetails(prescriptionDetails);
            addEditPrescriptionController.setStaffs(staff);
            addEditPrescriptionController.setPatients(patients);
            addEditPrescriptionController.setServices(services);
            addEditPrescriptionController.setAllServiceItems(serviceItems);


            addEditPrescriptionController.initializeCombo();
            if(isEditMode) {
                addEditPrescriptionController.initializeForm(prescription);
            }
            addEditPrescriptionController.setDialogStage(dialogStage);
            addEditPrescriptionController.setEditMode(isEditMode);
            dialogStage.showAndWait();

            if(addEditPrescriptionController.isSaved()){
                refreshTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
