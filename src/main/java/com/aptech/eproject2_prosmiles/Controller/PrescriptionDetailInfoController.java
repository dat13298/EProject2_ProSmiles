package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDetailDAO;
import com.aptech.eproject2_prosmiles.Repository.ServiceDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PrescriptionDetailInfoController extends BaseController {
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_quanity;

    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_service_name;

    @FXML
    private Label lbl_unit;

    private PrescriptionDetail prescriptionDetail;
    private Prescription prescription;
    private Stage dialog;
    private MethodInterceptor methodInterceptor;
    private PrescriptionDetailController prescriptionDetailController;

    public void setPrescriptionDetailController(PrescriptionDetailController prescriptionDetailController) {
        this.prescriptionDetailController = prescriptionDetailController;
    }

    public void setDialogStage(Stage dialog) {
        this.dialog = dialog;
    }

    public void setPrescriptionDetailInfo(PrescriptionDetail prescriptionDetailClicked) {
        ServiceDAO serviceDAO = new ServiceDAO();
        this.prescriptionDetail = prescriptionDetailClicked;

        lbl_service_name.setText(serviceDAO.getById(prescriptionDetailClicked.getService().getId()).getName());
        lbl_unit.setText(prescriptionDetailClicked.getUnit());
        lbl_quanity.setText(String.valueOf(prescriptionDetailClicked.getQuantity()));
        lbl_price.setText(String.valueOf(prescriptionDetailClicked.getPrice()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        btn_edit.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleEditPrescriptionDetailInfo", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        btn_cancel.setOnAction(actionEvent -> dialog.close());
    }

    @RolePermissionRequired(roles = {"Manager", "Doctor"})
    public void handleEditPrescriptionDetailInfo(ActionEvent actionEvent) {
        boolean isEditMode = true;
        prescriptionDetailController.showAddEditPrescriptionDetailInfo(prescriptionDetail, isEditMode);
    }

}
