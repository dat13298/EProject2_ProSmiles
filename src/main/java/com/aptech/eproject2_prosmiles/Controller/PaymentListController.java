package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentListController extends BaseController{
    @FXML
    private TableColumn<Payment, Double> colAmount;
    @FXML
    private TableColumn<Payment, Integer> colId;
    @FXML
    private TableColumn<Payment, String> colPatientName;
    @FXML
    private TableColumn<Payment, String> colPaymentNumber;
    @FXML
    private TableColumn<Payment, String> colPaymentType;
    @FXML
    private TableView<Payment> tblPayment;
    @FXML
    private Button btnDelete;

    private ObservableList<Payment> paymentList;
    private PaymentDetailController paymentDetailController;
    private boolean isListView = true;

    private MethodInterceptor methodInterceptor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        PaymentDAO paymentDAO = new PaymentDAO();
        PatientDAO patientDAO = new PatientDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

        paymentList = paymentDAO.getAll();
        tblPayment.setEditable(true);

        colId.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getId();
            return new SimpleObjectProperty<>(id);
        });

        colPatientName.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            Prescription prescription = prescriptionDAO.getById(payment.getPrescription().getId());
            Patient patient = patientDAO.getById(prescription.getPatient().getId());
            prescription.setPatient(patient);
            payment.setPrescription(prescription);

            return new SimpleStringProperty(patient.getName());
        });

        colPaymentNumber.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new SimpleStringProperty(payment.getBillNumber());
        });

        colAmount.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new SimpleObjectProperty<Double>(payment.getTotalAmount());
        });

        colPaymentType.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new SimpleStringProperty(payment.getPaymentType().getValue());
        });

        tblPayment.setItems(paymentList);

        tblPayment.setRowFactory(tv -> {
            TableRow<Payment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2) {
                    Payment paymentClicked = row.getItem();
                    showPaymentDetail(paymentClicked, isListView);
                }
            });
            return row;
        });

        btnDelete.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleDelete", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleDelete(ActionEvent event) {
        Payment selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
        if(selectedPayment != null) {
            boolean confirmed = DialogHelper.showConfirmationDialog("Confirm for delete", "Do you want to DELETE this payment?");
            if(confirmed) {
                selectedPayment.setIsDeleted(EIsDeleted.INACTIVE);
                PaymentDAO paymentDAO = new PaymentDAO();
                paymentDAO.delete(selectedPayment);
                tblPayment.getItems().remove(selectedPayment);
                tblPayment.refresh();
            }
        }
    }

    public void showPaymentDetail(Payment payment, boolean isView){
        try{
            InputStream fxmlStream = getClass()
                    .getResourceAsStream("/com/aptech/eproject2_prosmiles/View/Payment/PaymentDetail.fxml");
            if(fxmlStream == null) {
                System.err.println("FXML file is not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(fxmlStream);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Payment Detail");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogStage.setScene(scene);
            PaymentDetailController controller = loader.getController();
            paymentDetailController = controller;
            controller.setPaymentDetail(payment);
            controller.setDialogStage(dialogStage);
            isListView = isView;
            controller.setPaymentListController(this);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showAddEditPayment(Payment payment){
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/aptech/eproject2_prosmiles/View/Payment/AddEditPayment.fxml")
            );
            Stage dialogStage = new Stage();
            PaymentDAO paymentDAO = new PaymentDAO();
            PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

            dialogStage.setTitle("Edit Payment");

            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(tblPayment.getScene().getWindow());

            dialogStage.setScene(new Scene(loader.load()));
            AddEditPaymentController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPayment(payment);

            controller.initializeForm();

            dialogStage.showAndWait();

            if(controller.getIsSaved()){
                if(isListView){
                    paymentDAO.update(payment);
                    prescriptionDAO.update(payment.getPrescription());
                }
                paymentDetailController.setPaymentDetail(payment);
            }

            if(isListView){
                paymentList.clear();
                paymentList = paymentDAO.getAll();
                tblPayment.setItems(paymentList);
                tblPayment.refresh();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
