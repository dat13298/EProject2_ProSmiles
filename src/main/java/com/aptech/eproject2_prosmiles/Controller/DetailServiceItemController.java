package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.BitSet;
import java.util.ResourceBundle;

public class DetailServiceItemController extends BaseController {

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Label txt_detail_service_description;

    @FXML
    private Label txt_detail_service_id;

    @FXML
    private Label txt_detail_service_name;

    @FXML
    private Label txt_detail_service_price;

    @FXML
    private Label txt_detail_service_quantity;

    @FXML
    private Label txt_detail_service_svid;

    @FXML
    private Label txt_detail_service_unit;

    private Stage dialogStage;

    private ServiceItem serviceItem;

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
        txt_detail_service_name.setText(serviceItem.getName());
        txt_detail_service_quantity.setText(String.valueOf(serviceItem.getQuantity()));
        txt_detail_service_description.setText(serviceItem.getDescription());
        txt_detail_service_price.setText(String.valueOf(serviceItem.getPrice()));
        txt_detail_service_unit.setText(serviceItem.getUnit());
        txt_detail_service_svid.setText(String.valueOf(serviceItem.getService().getId()));
        txt_detail_service_id.setText(String.valueOf(serviceItem.getId()));
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
        btn_add_cancel.setOnAction(event ->{
        if (dialogStage != null) {
            dialogStage.close();
        };
        })
        ;

        btn_edit.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Service/EditDetailServiceItem.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                EditDetailServiceItemController controller = loader.getController();
                controller.setDialogStage(stage);
                controller.setServiceItem(serviceItem);
                controller.setServiceItemDetailController(this);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
    public void refreshServiceItemDetails() {
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO(); // Khởi tạo DAO
        serviceItem = serviceItemDAO.getById(serviceItem.getId()); // Tìm lại serviceItem từ database
        setServiceItem(serviceItem); // Cập nhật lại các trường hiển thị
    }





}

