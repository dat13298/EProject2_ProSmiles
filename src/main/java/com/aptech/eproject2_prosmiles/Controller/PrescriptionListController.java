package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrescriptionListController extends BaseController{

    @FXML
    private TableView<Prescription> tbl_prescription;

    @FXML
    private TableColumn<Prescription, Integer> col_id;
    @FXML
    private TableColumn<Prescription, String> col_patient_name;
    @FXML
    private TableColumn<Prescription, String> col_description;
    @FXML
    private TableColumn<Prescription, String> col_create_at;
    @FXML
    private TableColumn<Prescription, String> col_status;

    @FXML
    private Button btn_add_new;

    @FXML
    private Button btn_delete;

    private ObservableList<Prescription> prescriptionList;
    private PrescriptionDetailController prescriptionDetailController;
    private MethodInterceptor methodInterceptor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        PatientDAO patientDAO = new PatientDAO();
        StaffDAO staffDAO = new StaffDAO();

        prescriptionList = prescriptionDAO.getAll();
        tbl_prescription.setEditable(true);

        col_id.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleObjectProperty<>(prescription.getId());
        });

        col_patient_name.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            Patient patient = patientDAO.getById(prescription.getPatient().getId());
            Staff staff = staffDAO.getById(prescription.getStaff().getId());
            prescription.setPatient(patient);
            prescription.setStaff(staff);
            return new SimpleStringProperty(prescription.getPatient().getName());
        });

        col_description.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getDescription());
        });

        col_create_at.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getCreatedAt().toString());
        });

        col_status.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new SimpleStringProperty(prescription.getStatus().getStatus());
        });

        tbl_prescription.setItems(prescriptionList);

        tbl_prescription.setRowFactory(tv -> {
            TableRow<Prescription> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    Prescription prescriptionClicked = row.getItem();
                    showPrescriptionDetail(prescriptionClicked, true);
                }
            });
            return row;
        });

        btn_add_new.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleAddPrescription", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        btn_delete.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleDeletePrescription", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleAddPrescription(ActionEvent actionEvent) {
        Prescription newPrescription = new Prescription();
        boolean isEditMode = false;
        showAddEditForm(newPrescription, isEditMode);
    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleDeletePrescription(ActionEvent actionEvent) {
        Prescription selectedPrescription = tbl_prescription.getSelectionModel().getSelectedItem();
        if (selectedPrescription != null) {
            boolean confirmed = DialogHelper.showConfirmationDialog("Confirm for delete", "Do you want to DELETE this Prescription?");
            if (confirmed) {
                selectedPrescription.setIsDeleted(EIsDeleted.INACTIVE);
                PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
                PaymentDAO paymentDAO = new PaymentDAO();

                // Get and delete all payments for the prescription
                ObservableList<Payment> paymentsForPrescription = paymentDAO.getPaymentByPrescriptionId(selectedPrescription.getId());
                for (Payment payment : paymentsForPrescription) {
                    payment.setIsDeleted(EIsDeleted.INACTIVE);  // Soft delete
                    paymentDAO.delete(payment);
                    deleteBillPDF(payment.getBillNumber());// Update each payment to mark it as deleted
                }

                // Delete the prescription from the database
                prescriptionDAO.delete(selectedPrescription);

                // Update the UI
                tbl_prescription.getItems().remove(selectedPrescription);  // Remove from the list
                tbl_prescription.refresh();  // Refresh the table view
            }
        }
    }

    public void showPrescriptionDetail(Prescription prescriptionClicked, boolean isDetailView) {
        try{
            InputStream fxmlStream = getClass().getResourceAsStream("/com/aptech/eproject2_prosmiles/View/Prescription/PrescriptionDetail.fxml");
            if(fxmlStream == null) {
                System.err.println("FXML file not found");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(fxmlStream);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Prescription Detail");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogStage.setScene(scene);

            PrescriptionDetailController detailController = fxmlLoader.getController();
            prescriptionDetailController = detailController;
            detailController.setPrescription(prescriptionClicked);
            detailController.setDetailDialogStage(dialogStage);
            detailController.setIsDetailView(isDetailView);
            detailController.setPrescriptionListController(this);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void showAddEditForm(Prescription prescription, boolean isEditMode) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/AddEditPrescription.fxml")
            );
            Stage dialogStage = new Stage();
            PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

            dialogStage.setTitle(isEditMode ? "Edit Prescription" : "Add Prescription");

            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(tablePrescription.getScene().getWindow());

            dialogStage.setScene(new Scene(loader.load()));
            AddEditPrescriptionController controller = loader.getController();

            controller.setDialogStage(dialogStage);
            controller.setPrescription(prescription);
            controller.setEditMode(isEditMode);

            controller.initializeForm();

            dialogStage.showAndWait();

            if(controller.getIsSaved()){
                if(isEditMode){
                    prescriptionDAO.update(prescription);
                    prescriptionDetailController.setPrescription(prescription);
                }
                prescriptionList.clear();
                prescriptionList = prescriptionDAO.getAll();
                tbl_prescription.setItems(prescriptionList);
                tbl_prescription.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteBillPDF(String billId) {
        String pdfFilePath = "billing/PaymentDetails_" + billId + ".pdf";  // Replace with actual path

        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            if (pdfFile.delete()) {
                System.out.println("Bill PDF for prescription ID " + billId + " was deleted.");
            } else {
                System.err.println("Failed to delete the bill PDF for prescription ID " + billId);
            }
        } else {
            System.err.println("Bill PDF for prescription ID " + billId + " not found.");
        }
    }


}
