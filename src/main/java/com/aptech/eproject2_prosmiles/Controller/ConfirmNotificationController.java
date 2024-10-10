package com.aptech.eproject2_prosmiles.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class ConfirmNotificationController implements Initializable {
    @FXML
    private Label lbl_message;
    @FXML
    private Button btn_confirm;
    @FXML
    private Button btn_cancel;

    private boolean isConfirmed  = false;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setMessage(String message) {
        this.lbl_message.setText(message);
    }

    public void hideCancelButton() {
        btn_cancel.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_confirm.setOnAction(event -> {
            isConfirmed = true;
            dialogStage.close();
        });
        btn_cancel.setOnAction(event -> {
            isConfirmed = false;
            dialogStage.close();
        });
    }
}
