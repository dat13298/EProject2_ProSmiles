package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class PrescriptionDetailController extends BaseController {
    @FXML
    private Button btn_add_new;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_payment;
    @FXML
    private TableColumn<PrescriptionDetail, Integer> col_id;
    @FXML
    private TableColumn<PrescriptionDetail, Double> col_price;
    @FXML
    private TableColumn<PrescriptionDetail, Integer> col_quantity;
    @FXML
    private TableColumn<PrescriptionDetail, String> col_service_name;
    @FXML
    private TableColumn<PrescriptionDetail, String> col_unit;
    @FXML
    private Label lbl_description;
    @FXML
    private Label lbl_patient_name;
    @FXML
    private Label lblStaffName;
    @FXML
    private Label lbl_status;
    @FXML
    private TableView<PrescriptionDetail> tbl_prescription_detail;


    private boolean isDetailView;
    private ObservableList<PrescriptionDetail> prescriptionDetailList;
    private Prescription prescription;
    private Stage detailDialogStage;
    private MethodInterceptor methodInterceptor;
    private PrescriptionListController prescriptionListController;
    private PrescriptionDetailInfoController prescriptionDetailInfoController;
    private PaymentListController paymentListController = new PaymentListController();

    public void setPrescription(Prescription prescriptionClicked) {
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        prescriptionDetailList = prescriptionDetailDAO.getPresDetailByPresId(prescriptionClicked.getId());

        this.prescription = prescriptionClicked;
        lbl_patient_name.setText(prescriptionClicked.getPatient().getName());
        lbl_description.setText(truncateText(prescriptionClicked.getDescription(), 20));
        lblStaffName.setText(prescriptionClicked.getStaff().toString());
        lbl_status.setText(prescriptionClicked.getStatus().getStatus());

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

    public void setIsDetailView(boolean detailView) {
        isDetailView = detailView;
        if(!detailView){
            btn_add_new.setVisible(false);
            btn_delete.setVisible(false);
            btn_edit.setVisible(false);
            btn_payment.setVisible(false);
        }
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
            btn_edit.setOnAction((ActionEvent event) -> {
                try {
                    methodInterceptor.invokeMethod("handleEditPrescription", event);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });

            btn_add_new.setOnAction((ActionEvent event) -> {
                try {
                    methodInterceptor.invokeMethod("handleAddPresDetail", event);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });

            btn_delete.setOnAction(event -> {
                try {
                    methodInterceptor.invokeMethod("handleDeletePresDetail", event);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });

            btn_payment.setOnAction(new EventHandler<ActionEvent>() {
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
        btn_cancel.setOnAction(event -> detailDialogStage.close());

    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleEditPrescription(ActionEvent actionEvent) {
        prescriptionListController.showAddEditForm(prescription, true);
    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleAddPresDetail(ActionEvent actionEvent) {
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setPrescription(prescription);
        boolean isEditMode = false;
        showAddEditPrescriptionDetailInfo(prescriptionDetail, isEditMode);
    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleDeletePresDetail(ActionEvent actionEvent) {
        PrescriptionDetail selectedPrescriptionDetail = tbl_prescription_detail.getSelectionModel().getSelectedItem();
        if(selectedPrescriptionDetail != null) {
            boolean confirmed = DialogHelper.showConfirmationDialog("Confirm for delete", "Do you want to DELETE this Prescription?");
            if (confirmed) {
                selectedPrescriptionDetail.setIsDeleted(EIsDeleted.INACTIVE);
                PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();
                prescriptionDetailDAO.delete(selectedPrescriptionDetail);//remove from the DB
                tbl_prescription_detail.getItems().remove(selectedPrescriptionDetail);//remove from the list
                tbl_prescription_detail.refresh();
            }
        }
    }

    private void setupTableColumn(ObservableList<PrescriptionDetail> prescriptionDetails) {
        ServiceDAO serviceDAO = new ServiceDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

        tbl_prescription_detail.setEditable(true);

        col_id.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<>(prescriptionDetail.getId());
        });

        col_service_name.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            Service service = serviceDAO.getById(prescriptionDetail.getService().getId());
            prescriptionDetail.setService(service);
            return new SimpleStringProperty(service.getName());
        });

        col_unit.setCellValueFactory(cellData-> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleStringProperty(prescriptionDetail.getUnit());
        });

        col_quantity.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<Integer>(prescriptionDetail.getQuantity());
        });

        col_price.setCellValueFactory(cellData -> {
            PrescriptionDetail prescriptionDetail = cellData.getValue();
            return new SimpleObjectProperty<Double>(prescriptionDetail.getPrice());
        });

        tbl_prescription_detail.setItems(prescriptionDetails);

        tbl_prescription_detail.setRowFactory(tv -> {
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
                tbl_prescription_detail.setItems(prescriptionDetailList);
                tbl_prescription_detail.refresh();
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
