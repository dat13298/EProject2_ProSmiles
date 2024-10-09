package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Repository.ServiceDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AddEditServiceController extends BaseController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_upload_image;

    @FXML
    private ComboBox<Service> cmb_belong_service;

    @FXML
    private ImageView imv_service_picture;

    @FXML
    private Label lb_belong_service;

    @FXML
    private Label lb_image_service;

    @FXML
    private Label lb_title_service_description;

    @FXML
    private Label lb_title_service_name;

    @FXML
    private TextArea txt_description_service;

    @FXML
    private TextField txt_service_name;

    private Stage dialogStage;
    private boolean saved = false;
    private Service service;
    private File selectedFile;
    private boolean isEditMode;

    ServiceDAO serviceDAO = new ServiceDAO();
    ObservableList<Service> services = serviceDAO.getAll();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_upload_image.setOnAction(this::handleSelectFile);
        btn_save.setOnAction(this::handleSave);
        btn_cancel.setOnAction(event -> dialogStage.close());

        // Thiết lập danh sách dịch vụ vào ComboBox
        cmb_belong_service.getItems().addAll(services);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }

    public void setService(Service service) {
        this.service = service;
        setFieldValues(service); // Gọi phương thức để thiết lập giá trị trường
    }

    public boolean getIsSaved() {
        return saved;
    }

    public void handleSelectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imv_service_picture.setImage(image);
        }
    }

    private void handleSave(ActionEvent event) {
        if (selectedFile != null) {
            String serviceName = txt_service_name.getText();
            if (serviceName != null && !serviceName.isEmpty()) {
                service.setName(serviceName);
            }
            String serviceDescription = txt_description_service.getText();
            if (serviceDescription != null && !serviceDescription.isEmpty()) {
                service.setDescription(serviceDescription);
            }

            File savedFile = saveImageToDirectory(selectedFile);
            String imagePath = "com/aptech/eproject2_prosmiles/Media/img_service/" + savedFile.getName();
            service.setImagePath(imagePath);

            // Lưu dịch vụ
            serviceDAO.save(service);
            saved = true;
            dialogStage.close();
        }
    }

    private void setFieldValues(Service service) {
        if (service != null) {
            txt_service_name.setText(service.getName());
            txt_description_service.setText(service.getDescription());
            // Nếu bạn muốn thiết lập hình ảnh, hãy sử dụng đường dẫn của hình ảnh hiện tại
            if (service.getImagePath() != null) {
                Image image = new Image(service.getImagePath());
                imv_service_picture.setImage(image);
            }
            // Cài đặt giá trị cho ComboBox nếu cần
            // cmb_belong_service.setValue(service.getBelongService());
        }
    }

    private File saveImageToDirectory(File selectedFile) {
        String destinationPath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/img_service/";
        File destinationDir = new File(destinationPath);
        File destinationFile = getFilePath(selectedFile, destinationDir);

        try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile;
    }

    private static File getFilePath(File selectedFile, File destinationDir) {
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        String newFileName = selectedFile.getName();
        File destinationFile = new File(destinationDir, newFileName);
        int counter = 1;

        while (destinationFile.exists()) {
            String fileNameWithoutExt = newFileName.substring(0, newFileName.lastIndexOf('.'));
            String fileExtension = newFileName.substring(newFileName.lastIndexOf('.'));
            newFileName = fileNameWithoutExt + "_" + counter + fileExtension;
            destinationFile = new File(destinationDir, newFileName);
            counter++;
        }
        return destinationFile;
    }
}
