package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceItemDetailController extends BaseController {

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Label lb_detail_service_description;

    @FXML
    private Label lb_detail_service_id;

    @FXML
    private Label lb_detail_service_name;

    @FXML
    private Label lb_detail_service_price;

    @FXML
    private Label lb_detail_service_quantity;

    @FXML
    private Label lb_detail_service_svid;

    @FXML
    private Label lb_detail_service_unit;

    private Stage dialogStage;

    private ServiceItem serviceItem;
    private MethodInterceptor methodInterceptor;

    public void setItemTextLabel(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
        lb_detail_service_name.setText(serviceItem.getName());
        lb_detail_service_quantity.setText(String.valueOf(serviceItem.getQuantity()));
        lb_detail_service_description.setText(serviceItem.getDescription());
        lb_detail_service_price.setText(String.valueOf(serviceItem.getPrice()));
        lb_detail_service_unit.setText(serviceItem.getUnit());
        lb_detail_service_svid.setText(String.valueOf(serviceItem.getService().getId()));
        lb_detail_service_id.setText(String.valueOf(serviceItem.getId()));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private ServiceDetailController serviceDetailController;

    public void setServiceDetailController(ServiceDetailController serviceDetailController) {
        this.serviceDetailController = serviceDetailController;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methodInterceptor = new MethodInterceptor(this);
        btn_add_cancel.setOnAction(event -> {
            if (dialogStage != null) {
                dialogStage.close();
            }
        })
        ;

        btn_edit.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleEditServiceItem", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleEditServiceItem(ActionEvent event) {
        serviceDetailController.showAddEditItem(true, serviceItem, this);
    }


}

