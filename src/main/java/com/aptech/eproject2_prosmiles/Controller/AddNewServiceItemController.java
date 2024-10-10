package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.aptech.eproject2_prosmiles.Controller.ServiceListController.selectedService;

public class AddNewServiceItemController extends BaseController {

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Button btn_add_save;

    @FXML
    private Label lb_title;

    @FXML
    private Label lb_title_service_item_description;

    @FXML
    private Label lb_title_service_item_name;

    @FXML
    private Label lb_title_service_item_price;

    @FXML
    private Label lb_title_service_item_quantity;

    @FXML
    private Label lb_title_service_item_unit;

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
    private boolean saved = false;
    private ServiceItem serviceItem;


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
        // Xử lý sự kiện nút Cancel
        btn_add_cancel.setOnAction(event -> {
            // Lấy Stage hiện tại và đóng cửa sổ
            Stage stage = (Stage) btn_add_cancel.getScene().getWindow();
            stage.close();
        });
    }

    private void handleSave(ActionEvent event) {
        String name = txt_add_service_item_name.getText();
        String unit = txt_add_service_item_unit.getText();
        String quantity = txt_add_service_item_quantity.getText();
        String price = txt_add_service_item_price.getText();
        String description = txt_add_service_item_description.getText();
        // Giả sử serviceId là ID của service đang được chọn
        int serviceId = selectedService.getId(); // service được chọn

        if (!name.isEmpty() && !unit.isEmpty() && !quantity.isEmpty() && !price.isEmpty() && !description.isEmpty()) {
            // Tạo ServiceItem mới
            ServiceItem newItem = new ServiceItem();
            newItem.setService(selectedService); // Gán Service được chọn
            newItem.setName(name);
            newItem.setUnit(unit);
            newItem.setQuantity(Integer.parseInt(quantity));
            newItem.setPrice(Double.parseDouble(price));
            newItem.setDescription(description);

            // Lưu vào database qua ServiceItemDAO
            ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
            serviceItemDAO.save(newItem);

            // Thêm vào danh sách hiển thị trong TableView
            serviceDetailController.addServiceItem(newItem);

            // Đóng cửa sổ
            Stage stage = (Stage) btn_add_save.getScene().getWindow();
            stage.close();
        } else {
            // Xử lý trường hợp dữ liệu không hợp lệ (ví dụ: hiện thông báo lỗi)
            System.out.println("Please fill all fields!");
        }
    }

}
