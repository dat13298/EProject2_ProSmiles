package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDetailDAO;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceDetailController extends BaseController {

    @FXML
    private Label lb_service_name;
    @FXML
    private Label lb_description;
    @FXML
    private Label lb_service_price;
    @FXML
    private ImageView imv_service_image;
    @FXML
    private TableView<ServiceItem> tbl_service_item;
    @FXML
    private TableColumn<ServiceItem, String> col_service_item_name;
    @FXML
    private TableColumn<ServiceItem, Integer> col_service_item_quantity;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_cancel;

    private ObservableList<ServiceItem> serviceItems = FXCollections.observableArrayList();

    private Stage dialogStage;
    private Service service;

    private com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO serviceItemDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
        serviceItems = serviceItemDAO.getAll();
        btn_edit.setOnAction(event -> {
            // Logic cho nút chỉnh sửa
        });

        btn_cancel.setOnAction(event -> dialogStage.close());


    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public double totalPrice(Service service) {
        double totalPrice = serviceItems.stream()
                .filter(item -> item.getService().getId() == service.getId())
                .mapToDouble(ServiceItem::getPrice)
                .sum();
        return totalPrice;
    }


    private ObservableList<ServiceItem> findServiceItemByServiceId(int id) {
        return FXCollections.observableArrayList(serviceItems.stream()
                .filter(p -> p.getService().getId() == id).toList()
        );
    }

    public void setService(Service service) {

        this.service = service;
        lb_service_name.setText(service.getName());
        lb_description.setText(service.getDescription());

        String imagePath = "/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imv_service_image.setImage(image);

        col_service_item_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_service_item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lb_service_price.setText(String.valueOf(totalPrice(service)));

        ObservableList<ServiceItem> newServiceItems = FXCollections.observableArrayList(findServiceItemByServiceId(service.getId()));
        tbl_service_item.setItems(newServiceItems);
        tbl_service_item.refresh();








    }
}
