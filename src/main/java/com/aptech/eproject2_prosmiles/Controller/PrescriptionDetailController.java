package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Entity.*;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EPaymentType;
import com.aptech.eproject2_prosmiles.Repository.*;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import java.util.Objects;


public class PrescriptionDetailController {
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnPayment;
    @FXML
    private TableColumn<PrescriptionDetail, Integer> colId;
    @FXML
    private TableColumn<PrescriptionDetail, Double> colPrice;
    @FXML
    private TableColumn<PrescriptionDetail, Integer> colQuantity;
    @FXML
    private TableColumn<PrescriptionDetail, String> colServiceName;
    @FXML
    private TableColumn<PrescriptionDetail, String> colUnit;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblStaffName;
    @FXML
    private Label lblStatus;
    @FXML
    private TableView<PrescriptionDetail> tblPrescriptionDetail;
    @FXML
    private Button btn_export_pdf;


    private ObservableList<PrescriptionDetail> prescriptionDetailList;
    private Prescription prescription;
    private Stage detailDialogStage;
    private PrescriptionListController prescriptionListController;
    private PrescriptionDetailInfoController prescriptionDetailInfoController;
    private PaymentListController paymentListController = new PaymentListController();

    public void setPrescription(Prescription prescriptionClicked) {
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        prescriptionDetailList = prescriptionDetailDAO.getPresDetailByPresId(prescriptionClicked.getId());

        this.prescription = prescriptionClicked;
        lblPatientName.setText(prescriptionClicked.getPatient().getName());
        lblDescription.setText(truncateText(prescriptionClicked.getDescription(), 20));
        lblStaffName.setText(prescriptionClicked.getStaff().toString());
        lblStatus.setText(prescriptionClicked.getStatus().getStatus());

        setupTableColumn(prescriptionDetailList);
    }

    public String truncateText(String text, int maxLength) {
        if (text != null && text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        return text;
    }


    public void setDetailDialogStage(Stage dialogStage) {
        this.detailDialogStage = dialogStage;
    }

    public void setPrescriptionListController(PrescriptionListController prescriptionListController) {
        this.prescriptionListController = prescriptionListController;
    }

    @FXML
    public void initialize() {
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        btnEdit.setOnAction(event -> {
            prescriptionListController.showAddEditForm(prescription, true);
        });

        btnCancel.setOnAction(event -> detailDialogStage.close());

        btnAddNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                prescriptionDetail.setPrescription(prescription);
                boolean isEditMode = false;
                showAddEditPrescriptionDetailInfo(prescriptionDetail, isEditMode);
            }
        });

        btnDelete.setOnAction(event -> {
            PrescriptionDetail selectedPrescriptionDetail = tblPrescriptionDetail.getSelectionModel().getSelectedItem();
            if(selectedPrescriptionDetail != null) {
                boolean confirmed = DialogHelper.showConfirmationDialog("Confirm for delete", "Do you want to DELETE this Prescription?");
                if (confirmed) {
                    selectedPrescriptionDetail.setIsDeleted(EIsDeleted.INACTIVE);
                    prescriptionDetailDAO.delete(selectedPrescriptionDetail);//remove from the DB
                    tblPrescriptionDetail.getItems().remove(selectedPrescriptionDetail);//remove from the list
                    tblPrescriptionDetail.refresh();
                }
            }
        });

        btnPayment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Payment payment = new Payment();
                int maxId = paymentDAO.getAll().stream()
                                .mapToInt(Payment::getId).max().orElse(0);
                int rlId = maxId + 1;
                payment.setPrescription(prescription);
                payment.setBillNumber("BN00" + rlId);
                payment.setPaymentType(EPaymentType.CASH);
                payment.setTotalAmount(sumTotalPricePrescriptionDetail(prescription));
                paymentDAO.save(payment);
                paymentListController.showPaymentDetail(payment, false);
            }
        });
    }


    private void setupTableColumn(ObservableList<PrescriptionDetail> prescriptionDetails) {
        ServiceDAO serviceDAO = new ServiceDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

        tblPrescriptionDetail.setEditable(true);

        colId.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<>(prescriptionDetail.getId());
        });

        colServiceName.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            Service service = serviceDAO.getById(prescriptionDetail.getService().getId());
            prescriptionDetail.setService(service);
            return new SimpleStringProperty(service.getName());
        });

        colUnit.setCellValueFactory(cellData-> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleStringProperty(prescriptionDetail.getUnit());
        });

        colQuantity.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<Integer>(prescriptionDetail.getQuantity());
        });

        colPrice.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<Double>(prescriptionDetail.getPrice());
        });

        tblPrescriptionDetail.setItems(prescriptionDetails);

        tblPrescriptionDetail.setRowFactory(tv -> {
            TableRow<PrescriptionDetail> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    PrescriptionDetail prescriptionDetailClicked = row.getItem();
                    showPresDetailInfo(prescriptionDetailClicked);
                }
            });
            return row;
        });
    }

    private void showPresDetailInfo(PrescriptionDetail prescriptionDetailClicked) {
        try{
            InputStream fxmlStream = getClass().getResourceAsStream("/com/aptech/eproject2_prosmiles/View/Prescription/PrescriptionDetailInfo.fxml");
            if(fxmlStream == null){
                System.err.println("FXML file not found");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(fxmlStream);
            Stage dialogDetailInfo = new Stage();
            dialogDetailInfo.setTitle("Prescription Detail");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogDetailInfo.setScene(scene);

            PrescriptionDetailInfoController detailInfoController = fxmlLoader.getController();
            prescriptionDetailInfoController = detailInfoController;
            detailInfoController.setPrescriptionDetailInfo(prescriptionDetailClicked);
            detailInfoController.setDialogStage(dialogDetailInfo);
            detailInfoController.setPrescriptionDetailController(this);

            dialogDetailInfo.initModality(Modality.WINDOW_MODAL);
            dialogDetailInfo.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showAddEditPrescriptionDetailInfo(PrescriptionDetail prescriptionDetail, boolean isEditMode) {
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/aptech/eproject2_prosmiles/View/Prescription/AddEditPrescriptionInfor.fxml")
            );
            Stage dialogAddEditInfo = new Stage();
            PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();

            dialogAddEditInfo.setTitle(isEditMode ? "Edit Prescription Detail" : "Add Prescription Detail");

            dialogAddEditInfo.initModality(Modality.WINDOW_MODAL);
//            dialogAddEditInfo.initOwner(tblPrescriptionDetail.getScene().getWindow());

            dialogAddEditInfo.setScene(new Scene(loader.load()));
            AddNewPrescriptionInfoController controller = loader.getController();
            controller.setDialogStage(dialogAddEditInfo);
            controller.setPrescriptionDetail(prescriptionDetail);
            controller.setEditMode(isEditMode);

            controller.initializeForm();

            dialogAddEditInfo.showAndWait();

            System.out.println("IsSaved: " + controller.getIsSaved());

            if(controller.getIsSaved()){
                if(isEditMode){
                    prescriptionDetailDAO.update(prescriptionDetail);
                    prescriptionDetailInfoController.setPrescriptionDetailInfo(prescriptionDetail);
                }
                prescriptionDetailList.clear();
                prescriptionDetailList = prescriptionDetailDAO.getPresDetailByPresId(prescriptionDetail.getPrescription().getId());
                tblPrescriptionDetail.setItems(prescriptionDetailList);
                tblPrescriptionDetail.refresh();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private Double sumTotalPricePrescriptionDetail(Prescription prescription){
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        prescriptionDetailList = prescriptionDetailDAO.getPresDetailByPresId(prescription.getId());

        return prescriptionDetailList.stream()
                .map(PrescriptionDetail::getPrice)
                .reduce(0.0, Double::sum);
    }


}
