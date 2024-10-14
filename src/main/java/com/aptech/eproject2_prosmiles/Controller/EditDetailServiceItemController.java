package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditDetailServiceItemController extends BaseController {

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Button btn_edit;


    @FXML
    private TextArea txt_edit_service_item_description;

    @FXML
    private TextField txt_edit_service_item_id;

    @FXML
    private TextField txt_edit_service_item_name;

    @FXML
    private TextField txt_edit_service_item_price;

    @FXML
    private TextField txt_edit_service_item_quantity;

    @FXML
    private TextField txt_edit_service_item_svid;

    @FXML
    private TextField txt_edit_service_item_unit;

    private DetailServiceItemController setServiceItem;

    private ServiceItem serviceItem;

    private ServiceItemDAO serviceItemDAO;

    public EditDetailServiceItemController() {
        serviceItemDAO = new ServiceItemDAO(); // Khởi tạo DAO
    }


    public void setServiceItemDetailController(DetailServiceItemController setServiceItemDetailController) {
        this.setServiceItem = setServiceItemDetailController;
    }

    private Stage dialogStage;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
        txt_edit_service_item_name.setText(serviceItem.getName());
        txt_edit_service_item_quantity.setText(String.valueOf(serviceItem.getQuantity()));
        txt_edit_service_item_description.setText(serviceItem.getDescription());
        txt_edit_service_item_price.setText(String.valueOf(serviceItem.getPrice()));
        txt_edit_service_item_unit.setText(serviceItem.getUnit());
        txt_edit_service_item_svid.setText(String.valueOf(serviceItem.getService().getId()));
        txt_edit_service_item_id.setText(String.valueOf(serviceItem.getId()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        btn_add_cancel.setOnAction(event ->{
            if (dialogStage != null) {
                dialogStage.close();
            };
            dialogStage.close();
        })
        ;

        btn_edit.setOnAction(this::handleSave);

    }


    private void handleSave(ActionEvent event) {
        try {
            String name = txt_edit_service_item_name.getText();
            if (name == null || name.isEmpty()) {
                throw new Exception("name cannot be empty");
            }
            serviceItem.setName(name);

            String id = txt_edit_service_item_id.getText();
            if (id == null || id.isEmpty()) {
                throw new Exception("id cannot be empty");
            }
            serviceItem.setId(Integer.parseInt(id));

            String service_id = txt_edit_service_item_svid.getText();
            if (service_id == null || service_id.isEmpty()) {
                throw new Exception("service_id cannot be empty");
            }
            serviceItem.getService().setId(Integer.parseInt(service_id));

            String price = txt_edit_service_item_price.getText();
            if (price == null || price.isEmpty()) {
                throw new Exception("price invalid");
            }
            serviceItem.setPrice(Double.parseDouble(price));

            String unit = txt_edit_service_item_unit.getText();
            if (unit == null || unit.isEmpty()) {
                throw new Exception("unit cannot be empty");
            }
            serviceItem.setUnit(unit);

            String quantity = txt_edit_service_item_quantity.getText();
            if (quantity == null || quantity.isEmpty()) {
                throw new Exception("quantity cannot be empty");
            }
            serviceItem.setQuantity(Integer.parseInt(quantity));

            String description = txt_edit_service_item_description.getText();
            if (description == null || description.isEmpty()) {
                throw new Exception("description cannot be empty");
            }
            serviceItem.setDescription(description);


            serviceItemDAO.update(serviceItem);
            if (setServiceItem != null) {
                setServiceItem.refreshServiceItemDetails();
            }

            // Đóng dialog sau khi lưu
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
