package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.ServiceDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ServiceListController extends BaseController {
    @FXML
    private GridPane serviceGrid;
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnDelete;

    public static Service selectedService; // Dịch vụ được chọn để xóa
    private ObservableList<Service> services;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> services = serviceDAO.getAll();
        int column = 0;
        int row = 0;

        for (Service service : services) {
            String imagePath = "/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            assert inputStream != null;
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Label nameLabel = new Label(service.getName());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);
            vBox.getChildren().addAll(imageView, nameLabel);

            // Thêm VBox vào GridPane
            serviceGrid.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            // Sự kiện chọn dịch vụ khi nhấp vào VBox
            vBox.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Kiểm tra nếu click chuột 2 lần
                    openServiceDetail(service); // Mở chi tiết dịch vụ
                } else {
                    selectedService = service; // Lưu dịch vụ được chọn
                    highlightSelectedService(vBox); // Đánh dấu dịch vụ đang chọn
                }
            });

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        // Xử lý sự kiện nút Delete
        btnDelete.setOnAction(event -> {
            if (selectedService != null) {
                boolean confirmed = showConfirmationDialog("Confirm for delete", "Do you want to DELETE this service?");
                if (confirmed) {
                    selectedService.setIsDeleted(EIsDeleted.INACTIVE); // Đánh dấu là đã xóa
                    serviceDAO.delete(selectedService); // Xóa khỏi DB
                    removeServiceFromGrid(selectedService); // Xóa khỏi GridPane
                }
            }
        });

        // Thêm sự kiện cho nút Add New
        btnAddNew.setOnAction(event -> {
            Service newService = new Service(); // Tạo một dịch vụ mới
            boolean isEditMode = false; // Chế độ thêm mới
            showAddEditServiceForm(newService, isEditMode); // Gọi hàm để hiển thị biểu mẫu thêm
        });
    }

    // Hàm mở file ServiceDetail.fxml khi nhấp chuột đôi vào dịch vụ
    // Hàm mở file ServiceDetail.fxml khi nhấp chuột đôi vào dịch vụ
    private void openServiceDetail(Service service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/View/Service/ServiceDetail.fxml"));
            Stage detailStage = new Stage();
            detailStage.setTitle("Service Detail");

            detailStage.initModality(Modality.WINDOW_MODAL);
            detailStage.initOwner(serviceGrid.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            detailStage.setScene(scene);

            // Lấy controller và truyền đối tượng service vào
            ServiceDetailController controller = loader.getController();
            controller.setDialogStage(detailStage); // Truyền dialogStage vào controller
            controller.setService(service); // Truyền đối tượng service vào controller

            detailStage.showAndWait(); // Hiển thị dialog và chờ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Hàm chuyển sang trang AddEditService.fxml
    private void showAddEditServiceForm(Service service, boolean isEditMode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/View/Service/AddEditService.fxml"));
            Stage dialogStage = new Stage();
            ServiceDAO serviceDAO = new ServiceDAO();
            dialogStage.setTitle(isEditMode ? "Edit Service" : "Add Service");

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(serviceGrid.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            AddEditServiceController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEditMode(isEditMode);
            controller.setService(service); // Truyền đối tượng service vào controller

            dialogStage.showAndWait();

            if (controller.getIsSaved()) {
                if (isEditMode) {
                    serviceDAO.update(service); // Cập nhật dịch vụ
                } else {
                    services = serviceDAO.getAll(); // Lấy danh sách dịch vụ mới
                }
                refreshServiceGrid(); // Cập nhật GridPane với dịch vụ mới
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm hiện thông báo xác nhận
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Đánh dấu dịch vụ được chọn
    private void highlightSelectedService(VBox selectedVBox) {
        // Xóa highlight của các dịch vụ khác
        for (var node : serviceGrid.getChildren()) {
            if (node instanceof VBox) {
                node.getStyleClass().remove("selected-service");
            }
        }
        // Thêm highlight cho VBox được chọn
        selectedVBox.getStyleClass().add("selected-service");
    }

    // Xóa dịch vụ khỏi GridPane
    private void removeServiceFromGrid(Service service) {
        // Tìm và loại bỏ VBox chứa dịch vụ từ GridPane
        for (var node : serviceGrid.getChildren()) {
            if (node instanceof VBox) {
                Label label = (Label) ((VBox) node).getChildren().get(1); // Lấy Label chứa tên dịch vụ
                if (label.getText().equals(service.getName())) {
                    serviceGrid.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    // Phương thức refreshServiceGrid (cần được thêm vào)
    private void refreshServiceGrid() {
        // Xóa tất cả các dịch vụ hiện tại trong GridPane
        serviceGrid.getChildren().clear();

        // Tải lại danh sách dịch vụ
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> services = serviceDAO.getAll();
        int column = 0;
        int row = 0;

        for (Service service : services) {
            String imagePath = "/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            assert inputStream != null;
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Label nameLabel = new Label(service.getName());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);
            vBox.getChildren().addAll(imageView, nameLabel);

            // Thêm VBox vào GridPane
            serviceGrid.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}
