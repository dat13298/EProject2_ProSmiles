package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddEditPrescriptionController extends BaseController implements Initializable {


    @FXML
    private Button btnSave;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtStaffName;

    @FXML
    private TextField txtStatus;

    @FXML
    private TableColumn<ServiceItem, String> colName;

    @FXML
    private TableColumn<ServiceItem, String> colQuantity;

    @FXML
    private ComboBox<String> comboService;

    @FXML
    private TableView<ServiceItem> tblServiceItem;

    @FXML
    private Button btnCancel;

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
    private final StaffDAO staffDAO = new StaffDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final ServiceItemDAO serviceItemDAO = new ServiceItemDAO();

    private ObservableList<Service> service = serviceDAO.getAll();
    private ObservableList<Prescription> prescriptions = prescriptionDAO.getAll();
    private ObservableList<PrescriptionDetail> prescriptionDetails = prescriptionDetailDAO.getAll();
    private ObservableList<Staff> staffs = staffDAO.getAll();
    private ObservableList<Patient> patients = patientDAO.getAll();

    private ObservableList<Service> services;
    private ObservableList<ServiceItem> allServiceItems;

    private PrescriptionListController prescriptionListController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();

        comboService.getItems().clear();
        services = serviceDAO.getAll();

        for (Service service : services) {
            comboService.getItems().add(service.getName());
        }

        // Ensure the ComboBox does not have a default selection
        comboService.getSelectionModel().clearSelection();

        // Add a listener to update the table when a service is selected
        comboService.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                updateServiceItemsTable(newValue);
            }
        });

        // Set action for the "Save" button
        btnSave.setOnAction(event -> handleSave());
        btnCancel.setOnAction(event -> handleCancel());
    }


    private void handleSave() {
        String patientName = txtPatientName.getText();
        String staffName = txtStaffName.getText();
        String description = txtDescription.getText();
        String selectedService = comboService.getSelectionModel().getSelectedItem();
        String status = txtStatus.getText();


        if (patientName.isEmpty() || staffName.isEmpty() || description.isEmpty() || selectedService == null) {
            // Show an error message if any required field is empty
            System.out.println("Please fill all required fields.");
            return;
        }

        // Create a new prescription object
        Prescription newPrescription = new Prescription();
        PrescriptionDetail newPrescriptionDetail = new PrescriptionDetail();
        Patient patient = new Patient();
        Staff staff = new Staff();

        int maxId = prescriptions.stream()
                .mapToInt(Prescription::getId)
                .max()
                .orElse(0);
        newPrescription.setId(maxId + 1);
        newPrescription.setPatient(Optional.ofNullable(patientDAO.findByName(patientName).getFirst()));
        newPrescription.setStaff(Optional.ofNullable(staffDAO.findByName(staffName).getFirst()));
        newPrescription.setDescription(description);
        newPrescription.setStatus(EStatus.fromString(status));
        newPrescription.setCreatedAt(LocalDateTime.now());
        newPrescription.setUpdatedAt(LocalDateTime.now());
        newPrescription.setIsDeleted(EIsDeleted.ACTIVE);

        int maxDetailId = prescriptionDetails.stream()
                .mapToInt(PrescriptionDetail::getId)
                .max()
                .orElse(0);

        newPrescriptionDetail.setId(maxDetailId + 1);
        newPrescriptionDetail.setService(findServiceByName(selectedService));
        newPrescriptionDetail.setPrescription(Optional.of(newPrescription));
        newPrescriptionDetail.setUnit("1 session");
        newPrescriptionDetail.setQuantity(Integer.parseInt(txtQuantity.getText()));
        newPrescriptionDetail.setPrice(caculateTotalPrice(newPrescriptionDetail));
        newPrescriptionDetail.setCreatedAt(LocalDateTime.now());
        newPrescriptionDetail.setUpdatedAt(LocalDateTime.now());
        newPrescriptionDetail.setIsDeleted(EIsDeleted.ACTIVE);
        System.out.println(serviceDAO.findByName(selectedService).stream().findFirst());


        // Add the new prescription to the database
        prescriptionDAO.save(newPrescription);
        prescriptionDetailDAO.save(newPrescriptionDetail);

        // Show a success message or close the window
        System.out.println("Prescription added successfully!");

        // Optionally, clear form fields after saving
        clearForm();
        setupTableColumns();
        if (prescriptionListController != null) {
            prescriptionListController.refreshPrescriptionList();
        }
    }




    private void clearForm() {
        txtPatientName.clear();
        txtStaffName.clear();
        txtDescription.clear();
        comboService.getSelectionModel().clearSelection();
        tblServiceItem.getItems().clear();
    }

    private void setupTableColumns() {
        allServiceItems = serviceItemDAO.getAll();
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblServiceItem.setItems(allServiceItems);
    }

    private void handleCancel() {
        // Get the current stage (window) using the button's scene and close it
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }

    private void updateServiceItemsTable(String selectedServiceName) {
        // Fetch ServiceItem data corresponding to the selected service
        Optional<Service> selectedService = findServiceByName(selectedServiceName);

        if (selectedService.isPresent()) {
            ObservableList<ServiceItem> filteredServiceItems = FXCollections.observableArrayList(
                    allServiceItems.stream()
                            .filter(s -> s.getService().getId() == selectedService.get().getId())
                            .toList()
            );
            tblServiceItem.setItems(filteredServiceItems);
        }
    }

    private Optional<Service> findServiceByName(String name) {
        return services.stream().filter(s -> s.getName().equals(name)).findFirst();
    }

    public void setPrescriptionListController(PrescriptionListController controller) {
        this.prescriptionListController = controller;
    }

    public double caculateTotalPrice(PrescriptionDetail prescriptionDetail){
        double totalPrice = allServiceItems.stream()
                .filter(item -> item.getService().getId() == prescriptionDetail.getService().getId())
                .mapToDouble(ServiceItem::getPrice)
                .sum();
        return totalPrice;
    }


}
