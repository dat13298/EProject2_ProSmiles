package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TextField txtQuantity;

    @FXML
    private ComboBox<Staff> comboStaff;

    @FXML
    private ComboBox<EStatus> comboStatus;

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

    //DAO declare
    private PrescriptionDAO prescriptionDAO;
    private ServiceDAO serviceDAO;
    private PrescriptionDetailDAO prescriptionDetailDAO;
    private StaffDAO staffDAO;
    private PatientDAO patientDAO;
    private ServiceItemDAO serviceItemDAO;

    public void setPrescriptionDAO(PrescriptionDAO prescriptionDAO) {
        this.prescriptionDAO = prescriptionDAO;
    }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public void setPrescriptionDetailDAO(PrescriptionDetailDAO prescriptionDetailDAO) {
        this.prescriptionDetailDAO = prescriptionDetailDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public void setPatientDAO(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public void setServiceItemDAO(ServiceItemDAO serviceItemDAO) {
        this.serviceItemDAO = serviceItemDAO;
    }

    //Observable List
    private ObservableList<Prescription> prescriptions;
    private ObservableList<PrescriptionDetail> prescriptionDetails;
    private ObservableList<Staff> staffs;
    private ObservableList<Patient> patients;
    private ObservableList<Service> services;
    private ObservableList<ServiceItem> allServiceItems;

    public void setPrescriptions(ObservableList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void setPrescriptionDetails(ObservableList<PrescriptionDetail> prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }

    public void setStaffs(ObservableList<Staff> staffs) {
        this.staffs = staffs;
    }

    public void setPatients(ObservableList<Patient> patients) {
        this.patients = patients;
    }

    public void setServices(ObservableList<Service> services) {
        this.services = services;
    }

    public void setAllServiceItems(ObservableList<ServiceItem> allServiceItems) {
        this.allServiceItems = allServiceItems;
    }

    private Stage dialogStage;

    private Prescription prescription;
    private boolean isEditMode;
    private boolean saved = false;


    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }


    public boolean isSaved() {
        return saved;
    }

    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean getEditMode() {
        return isEditMode;
    }

    private PrescriptionDetailController prescriptionDetailController;

    public void setPrescriptionDetailController(PrescriptionDetailController prescriptionDetailController) {
        this.prescriptionDetailController = prescriptionDetailController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add a listener to update the table when a service is selected
        comboService.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                updateServiceItemsTable(newValue);
            }
        });

        // Set action for the "Save" button
        btnSave.setOnAction(event -> handleSave());
        btnCancel.setOnAction(event -> dialogStage.close());
    }

    public void initializeForm(Prescription editPrescription){
        this.prescription = editPrescription;
        PrescriptionDetail prescriptionDetail = findPrescriptionDetailByPrescriptionId(editPrescription.getId());
        System.out.println(editPrescription.getStaff());
        txtPatientId.setText(String.valueOf(editPrescription.getPatient().getId()));
        comboStaff.setValue(staffDAO.getById(editPrescription.getPatient().getId()));
        txtDescription.setText(String.valueOf(editPrescription.getDescription()));
        comboStatus.setValue(editPrescription.getStatus());
        comboService.setValue(serviceDAO.getById(prescriptionDetail.getService().getId()).getName());
        System.out.println(serviceDAO.getById(prescriptionDetail.getService().getId()).getName());
        txtQuantity.setText(String.valueOf(prescriptionDetail.getQuantity()));
    }

    public void initializeCombo(){
            setupTableColumns();
            comboService.getItems().clear();

            if (services != null) {
                for (Service service : services) {
                    comboService.getItems().add(service.getName());
                }
            } else {
                System.out.println("Services list is null");
            }

            if (staffs != null) {
                for (Staff staff : staffs) {
                    comboStaff.getItems().add(staff);
                }
            } else {
                System.out.println("Staff list is null");
            }

        for (EStatus status : EStatus.values()) {
            comboStatus.getItems().add(status);
        }

        // Ensure the ComboBox does not have a default selection
            comboService.getSelectionModel().clearSelection();


    }


    private void handleSave() {
        Prescription currentPrescription = prescription;

        String patientId = txtPatientId.getText().trim();
        Staff staffName = comboStaff.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();
        String selectedService = comboService.getSelectionModel().getSelectedItem();
        String status = comboStatus.getSelectionModel().getSelectedItem().getStatus();


        if (patientId.isEmpty() || staffName == null || description.isEmpty() || selectedService == null || status == null) {
            // Show an error message if any required field is empty
            System.out.println("Please fill all required fields.");
            return;
        }else{
            // Create a new prescription object
            Prescription newPrescription = new Prescription();
            PrescriptionDetail newPrescriptionDetail = new PrescriptionDetail();


            int maxId = prescriptions.stream()
                    .mapToInt(Prescription::getId)
                    .max()
                    .orElse(0);

            if(getEditMode()){
                newPrescription.setId(currentPrescription.getId());
            }else {
                newPrescription.setId(maxId + 1);
            }
            newPrescription.setPatient(patientDAO.getById(Integer.parseInt(patientId)));
            newPrescription.setStaff(staffName);
            newPrescription.setDescription(description);
            newPrescription.setStatus(EStatus.fromString(status));
            newPrescription.setCreatedAt(LocalDateTime.now());
            newPrescription.setUpdatedAt(LocalDateTime.now());
            newPrescription.setIsDeleted(EIsDeleted.ACTIVE);

            int maxDetailId = prescriptionDetails.stream()
                    .mapToInt(PrescriptionDetail::getId)
                    .max()
                    .orElse(0);

            if(getEditMode()){
                newPrescriptionDetail = findPrescriptionDetailByPrescriptionId(currentPrescription.getId());
                newPrescriptionDetail.setId(newPrescriptionDetail.getId());
            }else{
                newPrescriptionDetail.setId(maxDetailId + 1);
            }
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
            if(getEditMode()){
                prescriptionDAO.update(newPrescription);
                prescriptionDetailDAO.update(newPrescriptionDetail);
            }else{
                prescriptionDAO.save(newPrescription);
                prescriptionDetailDAO.save(newPrescriptionDetail);
            }

            //Alerting success added
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);

            if(getEditMode()){
                alert.setContentText("Prescription updated successfully.");
            }else {
                alert.setContentText("Prescription added successfully.");
            }

            alert.showAndWait();

            // Show a success message or close the window
            System.out.println("Prescription added successfully!");

            // Optionally, clear form fields after saving
            clearForm();
            setupTableColumns();

            saved = true;
            dialogStage.close();
        }

    }


    private void clearForm() {
        txtPatientId.clear();
        comboStaff.getSelectionModel().clearSelection();
        txtDescription.clear();
        comboService.getSelectionModel().clearSelection();
        tblServiceItem.getItems().clear();
        txtQuantity.clear();
        comboStatus.getSelectionModel().clearSelection();
    }

    private PrescriptionDetail findPrescriptionDetailByPrescriptionId(int id) {
        return prescriptionDetails.stream()
                        .filter(p -> p.getPrescription().getId() == id)
                        .findFirst().orElse(null);
    }

    private void setupTableColumns() {
        serviceItemDAO = new ServiceItemDAO();
        allServiceItems = serviceItemDAO.getAll();
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblServiceItem.setItems(allServiceItems);
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



    public double caculateTotalPrice(PrescriptionDetail prescriptionDetail){
        double totalPrice = allServiceItems.stream()
                .filter(item -> item.getService().getId() == prescriptionDetail.getService().getId())
                .mapToDouble(ServiceItem::getPrice)
                .sum();
        return totalPrice;
    }
}
