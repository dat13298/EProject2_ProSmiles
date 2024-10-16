package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private ObservableList<ServiceItem> serviceItems;
    private Stage dialogStage;
    private Service service = new Service();
    private ServiceListController serviceListController;
    private MethodInterceptor methodInterceptor;

    public void setServiceListController(ServiceListController serviceListController) {
        this.serviceListController = serviceListController;
    }

    private com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO serviceItemDAO;

    public void setServiceItems(ObservableList<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        serviceItemDAO = new ServiceItemDAO();
        serviceItems = serviceItemDAO.getServiceItemsByServiceId(service.getId());

        btn_edit.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleEditService", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        btn_cancel.setOnAction(event -> dialogStage.close());

        btn_add_new_service_item.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleAddServiceItem", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        btn_delete_service_item.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleDeleteServiceItem", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        tbl_service_item.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nếu nhấp đúp
                showServiceItemDetail();
            }
        });
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleEditService(ActionEvent event) {
        serviceListController.showAddEditServiceForm(service, true, this);
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleAddServiceItem(ActionEvent event) {
        boolean isEditMode = false;
        showAddEditItem(isEditMode, null, null);
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleDeleteServiceItem(ActionEvent event) {
        ServiceItem selectedServiceItem = tbl_service_item.getSelectionModel().getSelectedItem();
        if (selectedServiceItem != null) {
            boolean confirmed = showConfirmationDialog("Confirm for delete", "Do you want to DELETE this service item?");
            if (confirmed) {
                // Lưu giá của ServiceItem trước khi xóa
                double priceToRemove = selectedServiceItem.getPrice();

                selectedServiceItem.setIsDeleted(EIsDeleted.INACTIVE);
                serviceItemDAO.delete(selectedServiceItem);
                tbl_service_item.getItems().remove(selectedServiceItem);
                tbl_service_item.refresh();

                // Cập nhật giá tổng sau khi xoá ServiceItem
                double newTotalPrice = totalPrice(service) - priceToRemove;
                lb_service_price.setText(String.format("%.2f", newTotalPrice));
            }
        }
    }


    private void showServiceItemDetail() {
        ServiceItem selectedItem = tbl_service_item.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/com/aptech/eproject2_prosmiles/View/Service/ServiceItemDetail.fxml"));
                Parent detailServiceItemRoot = loader.load();

                ServiceItemDetailController controller = loader.getController();
                controller.setItemTextLabel(selectedItem);

                Stage stage = new Stage();
                Scene scene = new Scene(detailServiceItemRoot);
                stage.setScene(scene);

                ServiceItemDetailController detailServiceItemController = loader.getController();
                detailServiceItemController.setDialogStage(stage);
                detailServiceItemController.setItemTextLabel(selectedItem);
                detailServiceItemController.setServiceDetailController(this);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void showAddEditItem(boolean isEditMode, ServiceItem serviceItem, ServiceItemDetailController serviceItemDetailController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aptech/eproject2_prosmiles/View/Service/AddNewServiceItem.fxml"));
            Parent addNewServiceItemRoot = loader.load();
            AddEditServiceItemController addEditServiceItemController = loader.getController();
            addEditServiceItemController.setServiceDetailController(this);
            addEditServiceItemController.setEditMode(isEditMode);
            if (isEditMode) {
                addEditServiceItemController.setServiceItem(serviceItem);
                addEditServiceItemController.setServiceItemTextField();
                addEditServiceItemController.setServiceItemDetailController(serviceItemDetailController);

            }
            Stage stage = new Stage();
            stage.setScene(new Scene(addNewServiceItemRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (service == null || serviceItems == null) {
            return 0.0;
        }

        return serviceItems.stream()
                .filter(item -> item.getService() != null && item.getService().getId() == service.getId())
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

        String imagePath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
        File file = new File(imagePath);
        if (file.exists()) {
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

        ObservableList<ServiceItem> newServiceItems = FXCollections.observableArrayList(findServiceItemByServiceId(service.getId()));
        tbl_service_item.setItems(newServiceItems);
        tbl_service_item.refresh();

        lb_service_price.setText(String.format("%.2f", totalPrice(service)));
    }


    public void addServiceItem(ServiceItem item) {
        if (item.getService().getId() == service.getId()) {
            serviceItems.add(item);
            refreshServiceItems();
            lb_service_price.setText(String.format("%.2f", totalPrice(service)));
        }
    }


    private ObservableList<ServiceItem> findServiceItemByServiceId(int id) {
        return FXCollections.observableArrayList(serviceItemDAO.getAll().stream()
                .filter(p -> p.getService().getId() == id)
                .toList());
    }


}
