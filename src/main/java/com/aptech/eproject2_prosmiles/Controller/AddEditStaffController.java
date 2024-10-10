package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Repository.RoleDAO;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    private Label lb_notify;
    @FXML
    private ComboBox<Role> cmb_role;

    private Stage dialogStage;
    private boolean isEditMode;
    private boolean saved = false;
    private Staff staff;
    private File selectedFile;

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
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    public void initializeForm() {
        if (staff != null) {
            txt_first_name.setText(staff.getFirstName());
            txt_last_name.setText(staff.getLastName());
            txt_email.setText(staff.getEmail());
            txt_phone.setText(staff.getPhone());
            txt_address.setText(staff.getAddress());
            txt_age.setText(String.valueOf(staff.getAge()));
            cmb_gender.setValue(staff.getEGender());
            cmb_role.setValue(staff.getRole());

            if (staff.getImagePath() != null) {
                Image image = new Image(new File(staff.getImagePath()).toURI().toString());
                imv_staff_picture.setImage(image);
            }
        }

        btn_add_picture.setText(isEditMode ? "Change Picture" : "Add Picture");
    }

    public void handleSelectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                if (bufferedImage != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    imv_staff_picture.setImage(image);
                } else throw new IOException("File not found");
            } catch (IOException e) {
                lb_notify.setText("File not found");
            }
        }
    }


    private void handleSave(ActionEvent event) {
        try {
            if (selectedFile == null) {
                throw new Exception("No file selected. Please choose an image.");
            }
            String firstName = txt_first_name.getText();
            if (firstName == null || firstName.isEmpty()) {
                throw new Exception("First name cannot be empty");
            }
            staff.setFirstName(firstName);

            String lastName = txt_last_name.getText();
            if (lastName == null || lastName.isEmpty()) {
                throw new Exception("Last name cannot be empty");
            }
            staff.setLastName(lastName);

            String email = txt_email.getText();
            if (!Validation.isEmailValid(email)) {
                throw new Exception("Email invalid");
            }
            staff.setEmail(email);

            String phone = txt_phone.getText();
            if (!Validation.isPhoneNumberValid(phone)) {
                throw new Exception("Phone number invalid");
            }
            staff.setPhone(phone);

            String address = txt_address.getText();
            if (address == null || address.isEmpty()) {
                throw new Exception("Address cannot be empty");
            }
            staff.setAddress(address);

            String age = txt_age.getText();
            if (age == null || age.isEmpty()) {
                throw new Exception("Age cannot be empty");
            }
            staff.setAge(Integer.parseInt(age));

            String password = txt_password.getText();
            if (password == null || password.length() < 8) {
                throw new Exception("Password must be at least 8 characters");
            }
            staff.setPassword(password);

            if (cmb_gender.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Gender cannot be empty");
            }
            staff.setEGender(cmb_gender.getSelectionModel().getSelectedItem());

            if (cmb_role.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Role cannot be empty");
            }
            staff.setRole(cmb_role.getSelectionModel().getSelectedItem());

            File savedFile = saveImageToDirectory(selectedFile);
            if (savedFile == null) {
                throw new Exception("Failed to save image");
            }
            String imagePath = "/com/aptech/eproject2_prosmiles/Media/StaffImage/" + savedFile.getName();
            staff.setImagePath(imagePath);

            boolean registerSuccess = AuthenticationService.register(staff);

            DialogHelper.showNotificationDialog(
                    registerSuccess ? "Notification" : "Error",
                    registerSuccess ? "Create new staff successfully" : "Failed to create new staff"
            );
            saved = true;
            dialogStage.close();

        } catch (Exception e) {
            lb_notify.setText(e.getMessage());
        }
    }


    private File saveImageToDirectory(File selectedFile) {
        String destinationPath = "src/main/resources/com/aptech/eproject2_prosmiles/Media/StaffImage/";
        File destinationDir = new File(destinationPath);
        File destinationFile = getFile(selectedFile, destinationDir);

        try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            lb_notify.setText("Failed to save image");
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
