package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    protected Staff currentStaff;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /*LOAD CURRENT STAFF*/
    public void loadCurrentStaff() {
        currentStaff = new Staff();

    }
}
