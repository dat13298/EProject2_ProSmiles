package com.aptech.eproject2_prosmiles;

import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDetailDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProSmilesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ProSmilesApplication.class
                .getResource("/com/aptech/eproject2_prosmiles/View/LoginForm.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}