package com.aptech.eproject2_prosmiles.Controller;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    private TableColumn<?, ?> col_service_item_description;
    @FXML
    private TableColumn<?, ?> col_service_item_id;
    @FXML
    private TableColumn<?, ?> col_service_item_price;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_delete_service_item;
    @FXML
    private Button btn_add_new_service_item;
    private ServiceItem selectedServiceItem;

    private ObservableList<ServiceItem> serviceItems = FXCollections.observableArrayList();

    private Stage dialogStage;
    private Service service;

    private com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO serviceItemDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
        serviceItems = serviceItemDAO.getAll();

        // Xử lý sự kiện nút Edit
        btn_edit.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Service/EditServiceDetail.fxml"));
                Parent editServiceDetailRoot = loader.load();

                // Chuyển đến màn hình mới
                Stage stage = (Stage) btn_edit.getScene().getWindow();
                Scene scene = new Scene(editServiceDetailRoot);
                stage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Xử lý sự kiện nút Cancel
        btn_cancel.setOnAction(event -> dialogStage.close());

        // Xử lý sự kiện nút Add New Service Item
        btn_add_new_service_item.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Service/AddNewServiceItem.fxml"));
                Parent addNewServiceItemRoot = loader.load();

                AddNewServiceItemController controller = loader.getController();
                controller.setServiceDetailController(this); // Truyền ServiceDetailController

                Stage stage = new Stage();
                stage.setScene(new Scene(addNewServiceItemRoot));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btn_delete_service_item.setOnAction(event -> {
            ServiceItem selectedService = tbl_service_item.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                boolean confirmed = showConfirmationDialog("Confirm for delete", "Do you want to DELETE this staff?");
                if (confirmed) {
                    selectedService.setIsDeleted(EIsDeleted.INACTIVE);
                    serviceItemDAO.delete(selectedService);//remove from the DB
                    tbl_service_item.getItems().remove(selectedService);//remove from the list
                    tbl_service_item.refresh();
                }
            }
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

   /* public void loadServiceItems(int serviceId) {
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
        List<ServiceItem> items = serviceItemDAO.getServiceItemsByServiceId(serviceId); // Lấy danh sách từ database
        serviceItems.setAll(items); // Đặt dữ liệu vào ObservableList
        tbl_service_item.setItems(serviceItems); // Cập nhật TableView
    }*/



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
        col_service_item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_service_item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_service_item_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        lb_service_price.setText(String.valueOf(totalPrice(service)));

        ObservableList<ServiceItem> newServiceItems = FXCollections.observableArrayList(findServiceItemByServiceId(service.getId()));
        tbl_service_item.setItems(newServiceItems);
        tbl_service_item.refresh();

    }
    public void addServiceItem(ServiceItem item) {
        serviceItems.add(item);
        tbl_service_item.setItems(serviceItems); // Cập nhật TableView
        tbl_service_item.refresh(); // Làm mới TableView
    }

}
