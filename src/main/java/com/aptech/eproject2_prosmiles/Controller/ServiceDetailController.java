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
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
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
    private TableColumn<ServiceItem, String> col_service_item_description;
    @FXML
    private TableColumn<ServiceItem, Integer> col_service_item_id;
    @FXML
    private TableColumn<ServiceItem, Double> col_service_item_price;
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
    private Service service = new Service();
    private ServiceListController serviceListController;

    public void setServiceListController(ServiceListController serviceListController) {
        this.serviceListController = serviceListController;
    }

    private com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO serviceItemDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceItemDAO = new ServiceItemDAO();
        serviceItems = serviceItemDAO.getAll();
        btn_edit.setOnAction(event -> {
            serviceListController.showAddEditServiceForm(service, true, this);
        });
        btn_cancel.setOnAction(event -> dialogStage.close());

        btn_add_new_service_item.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Service/AddNewServiceItem.fxml"));
                Parent addNewServiceItemRoot = loader.load();
                AddNewServiceItemController controller = loader.getController();
                controller.setServiceDetailController(this);
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
                    serviceItemDAO.delete(selectedService);
                    tbl_service_item.getItems().remove(selectedService);
                    tbl_service_item.refresh();
                }
            }
        });
        tbl_service_item.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                ServiceItem selectedItem = tbl_service_item.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/com/aptech/eproject2_prosmiles/View/Service/DetailServiceItem.fxml"));
                        Parent detailServiceItemRoot = loader.load();

                        DetailServiceItemController controller = loader.getController();
                        controller.setServiceItem(selectedItem);
                        Stage stage = new Stage();
                        Scene scene = new Scene(detailServiceItemRoot);
                        stage.setScene(scene);
                        DetailServiceItemController detailServiceItemController = loader.getController();
                        detailServiceItemController.setDialogStage(stage);
                        detailServiceItemController.setServiceItem(selectedItem);
                        detailServiceItemController.setServiceDetailController(this);

                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public double totalPrice(Service service) {
        return serviceItems.stream()
                .filter(item -> item.getService().getId() == service.getId())
                .mapToDouble(ServiceItem::getPrice)
                .sum();
    }

    public void refreshServiceItems() {
        ObservableList<ServiceItem> filteredItems = findServiceItemByServiceId(service.getId());
        tbl_service_item.setItems(filteredItems);
        tbl_service_item.refresh();
    }



    public void refreshServiceDetails(Service serviceEdited) {
        lb_service_name.setText(serviceEdited.getName());
        lb_description.setText(serviceEdited.getDescription());

        // Cập nhật lại hình ảnh
        String imagePath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
        File file = new File(imagePath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            imv_service_image.setImage(image);
        } else {
            System.out.println("Image file not found: " + file.getAbsolutePath());
        }
    }



    public void setService(Service service) {
        this.service = service;
        lb_service_name.setText(service.getName());
        lb_description.setText(service.getDescription());

        // Sử dụng File thay vì getResourceAsStream
        String imagePath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
        File file = new File(imagePath);

        if (file.exists()) {
            // Tạo Image từ file URI
            Image image = new Image(file.toURI().toString());
            imv_service_image.setImage(image);
        } else {
            File defaultFile = new File("src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/tooth_extraction.jpg");
            if (defaultFile.exists()) {
                Image defaultImage = new Image(defaultFile.toURI().toString());
                imv_service_image.setImage(defaultImage);
            } else {
                System.out.println("Default image not found.");
            }
        }

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
        // Chỉ thêm mục nếu nó thuộc về dịch vụ đang chọn
        if (item.getService().getId() == service.getId()) {
            serviceItems.add(item);
            refreshServiceItems(); // Làm mới bảng để hiển thị mục mới
        }
    }
    private ObservableList<ServiceItem> findServiceItemByServiceId(int id) {
        return FXCollections.observableArrayList(serviceItemDAO.getAll().stream()
                .filter(p -> p.getService().getId() == id)
                .toList());
    }




}
