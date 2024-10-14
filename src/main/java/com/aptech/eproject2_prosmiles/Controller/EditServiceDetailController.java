package com.aptech.eproject2_prosmiles.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditServiceDetailController extends BaseController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_upload_image;

    @FXML
    private ImageView imv_edit_service_image;

    @FXML
    private Label lb_title;

    @FXML
    private Label lb_title_service_description;

    @FXML
    private Label lb_title_service_name;

    @FXML
    private Label lb_title_service_picture;

    @FXML
    private TextField txt_edit_service_description;

    @FXML
    private TextField txt_edit_service_name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_cancel.setOnAction(event -> {
            Stage stage = (Stage) btn_cancel.getScene().getWindow();
            stage.close();
        });


    }

}
