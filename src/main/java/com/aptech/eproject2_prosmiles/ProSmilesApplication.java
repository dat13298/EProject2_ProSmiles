package com.aptech.eproject2_prosmiles;

import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Service.AuthenticationService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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

        /*REGISTER STAFF FOR TEST LOGIN*/
       insertNewStaff();
    }

    public static void main(String[] args) {
        launch();
    }
    public static void insertNewStaff(){
        Staff staff = new Staff();
        Role role = new Role();
        role.setId(1);
        staff.setRole(Optional.of(role));
        staff.setFirstName("Alex");
        staff.setLastName("Mahome");
        staff.setEGender(EGender.MALE);
        staff.setPhone("123456");
        staff.setPassword("admin123");
        staff.setAddress("285 Doi Can");
        staff.setEmail("admin@gmail.com");
        staff.setAge(20);
        staff.setImagePath("imagesPath");
        if(AuthenticationService.register(staff)){
            System.out.println("Registration Successful");
        } else System.out.println("Registration Failed");
    }
}