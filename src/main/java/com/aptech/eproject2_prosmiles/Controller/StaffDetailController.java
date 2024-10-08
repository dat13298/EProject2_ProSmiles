package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffDetailController extends BaseController{
    @FXML
    private Label lbl_title_staff_name;
    @FXML
    private Label lbl_staff_name;
    @FXML
    private ImageView ava_staff;
    @FXML
    private Circle ava_clip;
    @FXML
    private Label lbl_title_staff_position;
    @FXML
    private Label lbl_staff_position;
    @FXML
    private Label lbl_title_staff_gender;
    @FXML
    private Label lbl_staff_gender;
    @FXML
    private Label lbl_title_staff_email;
    @FXML
    private Label lbl_staff_email;
    @FXML
    private Label lbl_title_staff_phone_number;
    @FXML
    private Label lbl_staff_phone_number;
    @FXML
    private Label lbl_title_staff_address;
    @FXML
    private Label lbl_staff_address;
    @FXML
    private Label lbl_age;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setStaffDetails(Staff staff) {
        lbl_staff_name.setText(staff.getFirstName() + " " + staff.getLastName());
        lbl_staff_email.setText(staff.getEmail());
        lbl_staff_gender.setText(staff.getEGender().getGender());
        lbl_staff_phone_number.setText(staff.getPhone());
        lbl_staff_address.setText(staff.getAddress());
        lbl_age.setText(String.valueOf(staff.getAge()));

    }
}
