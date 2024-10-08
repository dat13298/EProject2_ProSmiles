package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Repository.RoleDAO;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
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

public class AddEditStaffController extends BaseController {

    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_add_picture;
    @FXML
    private ImageView imv_staff_picture;
    @FXML
    private TextField txt_first_name;
    @FXML
    private TextField txt_last_name;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_phone;
    @FXML
    private TextField txt_address;
    @FXML
    private TextField txt_age;
    @FXML
    private PasswordField txt_password;
    @FXML
    private ComboBox<EGender> cmb_gender;
    @FXML
    private ComboBox<Role> cmb_role;

    private Stage dialogStage;
    private boolean isEditMode;
    private boolean saved = false;
    private Staff staff;
    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RoleDAO roleDAO = new RoleDAO();
        ObservableList<Role> roles = roleDAO.getAll();
        cmb_role.getItems().clear();
        cmb_role.getItems().addAll(roles);

        cmb_gender.getItems().clear();
        for (EGender gender : EGender.values()) {
            cmb_gender.getItems().add(gender);
        }
        btn_add_picture.setOnAction(this::handleSelectFile);
        btn_save.setOnAction(this::handleSave);
        btn_add_picture.setText(isEditMode ? "Change Picture" : "Add Picture");
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
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
            imv_staff_picture.setImage(image);
        }
    }


    private void handleSave(ActionEvent event) {
        if (selectedFile != null) {
            String firstName = txt_first_name.getText();
            if (firstName != null && !firstName.isEmpty()) {
                staff.setFirstName(firstName);
            }
            String lastName = txt_last_name.getText();
            if (lastName != null && !lastName.isEmpty()) {
                staff.setLastName(lastName);
            }
            String email = txt_email.getText();
            if (Validation.isEmailValid(email)) {
                staff.setEmail(email);
            }
            String phone = txt_phone.getText();
            if (Validation.isPhoneNumberValid(phone)) {
                staff.setPhone(phone);
            }
            String address = txt_address.getText();
            if (address != null && !address.isEmpty()) {
                staff.setAddress(address);
            }
            String age = txt_age.getText();
            if (age != null && !age.isEmpty()) {
                staff.setAge(Integer.parseInt(age));
            }
            String password = txt_password.getText();
            if (password != null && password.length() > 8) {
                staff.setPassword(password);
            }
            if (cmb_gender.getSelectionModel().getSelectedItem() != null) {
                EGender gender = cmb_gender.getSelectionModel().getSelectedItem();
                staff.setEGender(gender);
            }
            if (cmb_role.getSelectionModel().getSelectedItem() != null) {
                Role selectedRole = cmb_role.getSelectionModel().getSelectedItem();
                staff.setRole(selectedRole);
            }
            File savedFile = saveImageToDirectory(selectedFile); // move file to folder src
            String imagePath = "com/aptech/eproject2_prosmiles/Media/StaffImage/" + savedFile.getName();
            staff.setImagePath(imagePath);
            // insert staff here
            AuthenticationService.register(staff);
            saved = true;
            dialogStage.close();
        }
    }

    private File saveImageToDirectory(File selectedFile) {
        String destinationPath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/StaffImage/";
        File destinationDir = new File(destinationPath);
        File destinationFile = getFile(selectedFile, destinationDir);

        try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationFile;
    }

    private static File getFile(File selectedFile, File destinationDir) {
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
