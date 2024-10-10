package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.aptech.eproject2_prosmiles.Repository.PaymentDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentListController extends BaseController{

    @FXML
    private TableColumn<Payment, String> colAmount;

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

    //Observation Lists
    private ObservableList<Payment> payments = FXCollections.observableArrayList();
    private ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();

    //DAO
    private PaymentDAO paymentDAO;
    private PrescriptionDAO prescriptionDAO;
    private PatientDAO patientDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentDAO = new PaymentDAO();
        prescriptionDAO = new PrescriptionDAO();
        patientDAO = new PatientDAO();

        payments = paymentDAO.getAll();
        prescriptions = prescriptionDAO.getAll();

        setupTableColumns();
        tblPayment.setEditable(true);
        tblPayment.setItems(payments);


        tblPayment.setRowFactory(tv -> {
            TableRow<Payment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Payment paymentClicked = row.getItem();
                    showPaymentDetails(paymentClicked);
                }
            });
            return row;
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

    private void setupTableColumns(){
        colId.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("id"));

        colPatientName.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            Prescription prescription = prescriptionDAO.getById(payment.getPrescription().get().getId());
            Patient patient = patientDAO.getById(prescription.getPatient().getId());
            return new SimpleStringProperty(String.valueOf(patient.getName()));
        });

        colPaymentNumber.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(payment.getBillNumber()));
        });

        colAmount.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(payment.getTotalAmount()));
        });

        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
    }

    public void refreshTable() {
        tblPayment.getItems().clear();
        payments = paymentDAO.getAll();
        System.out.println("==== refreshing ===");
        payments.forEach(System.out::println);
        tblPayment.setItems(payments);
        tblPayment.refresh();
    }

    private void showPaymentDetails(Payment selectedPayment){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Payment/PaymentDetail.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Payment Details");

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tblPayment.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            PaymentDetailController controller = loader.getController();

            controller.setPaymentListControllerr(this);
            controller.setPayment(selectedPayment);
            controller.setUpDetail(selectedPayment);

            //Observable List
            controller.setPaymentList(payments);
            controller.setPrescriptionList(prescriptions);

            //DAO
            controller.setPaymentDAO(paymentDAO);
            controller.setPrescriptionDAO(prescriptionDAO);
            controller.setPatientDAO(patientDAO);

            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addEditPaymentForm(Payment payment, boolean isEditMode){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Payment/AddEditPayment.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(isEditMode ? "Add Payment Form" : "Edit Payment Form");

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tblPayment.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));
            AddEditPaymentController controller = loader.getController();

            //Observable List
            controller.setPaymentList(payments);
            controller.setPrescriptionList(prescriptions);

            //DAO
            controller.setPaymentDAO(paymentDAO);
            controller.setPrescriptionDAO(prescriptionDAO);
            controller.setPatientDAO(patientDAO);


        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
