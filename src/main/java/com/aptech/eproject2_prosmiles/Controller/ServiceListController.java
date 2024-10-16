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

import java.io.File;
import java.io.IOException;
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

    public static Service selectedService;
    private ObservableList<Service> services;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceDAO serviceDAO = new ServiceDAO();
        services = serviceDAO.getAll();
        int column = 0;
        int row = 0;

        for (Service service : services) {
            String imagePath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
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

            serviceGrid.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            vBox.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    openServiceDetail(service);
                } else {
                    selectedService = service;
                    highlightSelectedService(vBox);
                }
            });

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        btnDelete.setOnAction(event -> {
            if (selectedService != null) {
                boolean confirmed = showConfirmationDialog("Confirm for delete", "Do you want to DELETE this service?");
                if (confirmed) {
                    selectedService.setIsDeleted(EIsDeleted.INACTIVE);
                    serviceDAO.delete(selectedService);
                    removeServiceFromGrid(selectedService);
                }
            }
        });

        btnAddNew.setOnAction(event -> {
            Service newService = new Service();
            boolean isEditMode = false;
            showAddEditServiceForm(newService, isEditMode, null);
        });
    }

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

            ServiceDetailController controller = loader.getController();
            controller.setDialogStage(detailStage);
            controller.setService(service);
            controller.setServiceListController(this);
            detailStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddEditServiceForm(Service service, boolean isEditMode, ServiceDetailController serviceDetailController) {
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
            controller.setServiceDetailController(serviceDetailController);
            controller.setDialogStage(dialogStage);
            controller.setEditMode(isEditMode);
            controller.setService(service);
            controller.initializeForm();

            dialogStage.showAndWait();

            if (controller.getIsSaved()) {
                if (isEditMode) {
                    serviceDAO.update(service);
                }
                refreshServiceGrid();
            }
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

    private void removeServiceFromGrid(Service service) {
        for (var node : serviceGrid.getChildren()) {
            if (node instanceof VBox) {
                Label label = (Label) ((VBox) node).getChildren().get(1);
                if (label.getText().equals(service.getName())) {
                    serviceGrid.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    private void refreshServiceGrid() {
        serviceGrid.getChildren().clear();
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> services = serviceDAO.getAll();
        int column = 0;
        int row = 0;

        for (Service service : services) {
            String imagePath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/" + service.getImagePath();
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
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

            serviceGrid.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");
            vBox.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    openServiceDetail(service);
                } else {
                    selectedService = service;
                    highlightSelectedService(vBox);
                }
            });

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}
