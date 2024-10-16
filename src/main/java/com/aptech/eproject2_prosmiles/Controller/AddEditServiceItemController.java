package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.aptech.eproject2_prosmiles.Controller.ServiceListController.selectedService;

public class AddEditServiceItemController extends BaseController {

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Button btn_add_save;

    @FXML
    private TextArea txt_add_service_item_description;

    @FXML
    private TextField txt_add_service_item_name;

    @FXML
    private TextField txt_add_service_item_price;

    @FXML
    private TextField txt_add_service_item_quantity;

    @FXML
    private TextField txt_add_service_item_unit;
    private Stage dialogStage;
    private ServiceItem serviceItem;
    private boolean isEditMode;
    private ServiceItemDetailController serviceItemDetailController;

    public void setServiceItemDetailController(ServiceItemDetailController serviceItemDetailController) {
        this.serviceItemDetailController = serviceItemDetailController;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    private ServiceDetailController serviceDetailController;

    public void setServiceDetailController(ServiceDetailController controller) {
        this.serviceDetailController = controller;
    }



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        btn_add_save.setOnAction(this::handleSave);
        btn_add_cancel.setOnAction(event -> {
            Stage stage = (Stage) btn_add_cancel.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void handleSave(ActionEvent event) {
        handleSaveServiceItem();
    }

    public void setServiceItemTextField(){
        txt_add_service_item_name.setText(serviceItem.getName());
        txt_add_service_item_unit.setText(serviceItem.getUnit());
        txt_add_service_item_price.setText(String.valueOf(serviceItem.getPrice()));
        txt_add_service_item_quantity.setText(String.valueOf(serviceItem.getQuantity()));
        txt_add_service_item_description.setText(serviceItem.getDescription());
    }

    private void handleSaveServiceItem() {
        if (serviceItem == null) {
            serviceItem = new ServiceItem();
        }
        String name = txt_add_service_item_name.getText();
        String unit = txt_add_service_item_unit.getText();
        String quantity = txt_add_service_item_quantity.getText();
        String price = txt_add_service_item_price.getText();
        String description = txt_add_service_item_description.getText();

        if (!name.isEmpty() && !unit.isEmpty() && !quantity.isEmpty() && !price.isEmpty() && !description.isEmpty()) {
            serviceItem.setService(selectedService);
            serviceItem.setName(name);
            serviceItem.setUnit(unit);
            serviceItem.setQuantity(Integer.parseInt(quantity));
            serviceItem.setPrice(Double.parseDouble(price));
            serviceItem.setDescription(description);
            ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
            if(isEditMode) {
                serviceItemDAO.update(serviceItem);
                serviceItemDetailController.setItemTextLabel(serviceItem);
            }else {
                serviceItemDAO.save(serviceItem);
            }
            serviceDetailController.refreshServiceItems();

            Stage stage = (Stage) btn_add_save.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Please fill in all fields!");
        }
    }





}
