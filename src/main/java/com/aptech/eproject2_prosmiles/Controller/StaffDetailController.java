package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffDetailController extends BaseController {
    @FXML
    private Label lbl_staff_name;
    @FXML
    private Label lbl_staff_gender;
    @FXML
    private Label lbl_staff_email;
    @FXML
    private Label lbl_staff_phone_number;
    @FXML
    private Label lbl_staff_address;
    @FXML
    private Label lbl_age;
    @FXML
    private Label lbl_staff_position;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_cancel;
    @FXML
    private ImageView imv_staff_image;

    private Staff staff;
    private Stage dialogStage;
    private StaffListController staffListController;
    private MethodInterceptor methodInterceptor;

    public void setStaffListController(StaffListController staffListController) {
        this.staffListController = staffListController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);

        btn_edit.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleEditStaff", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleEditStaff(ActionEvent event) {
        staffListController.showAddEditForm(staff, true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStaffDetails(Staff staffClicked) {
        this.staff = staffClicked;
        lbl_staff_name.setText(staffClicked.getFirstName() + " " + staff.getLastName());
        lbl_staff_email.setText(staffClicked.getEmail());
        lbl_staff_gender.setText(staffClicked.getEGender().getGender());
        lbl_staff_phone_number.setText(staffClicked.getPhone());
        lbl_staff_address.setText(staffClicked.getAddress());
        lbl_age.setText(String.valueOf(staffClicked.getAge()));
        lbl_staff_position.setText(staffClicked.getRole().getTitle());

        File file = new File("src/main/resources" + staffClicked.getImagePath());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            imv_staff_image.setImage(image);
        } else {
            System.out.println("Image file not found.");
        }

    }
}
