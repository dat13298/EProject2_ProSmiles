package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ServiceDetailController extends BaseController {


    @FXML
    private TextField lbl_service_name;
    @FXML
    private TextField  lbl_service_price;
    @FXML
    private TextField  lbl_service_unit;
    @FXML
    private TextField  lbl_service_quantity;
    @FXML
    private TextField  lbl_service_description;
    @FXML
    private TextField lbl_service_dosage;
    @FXML
    private TextField instructionField;
    @FXML
    private TextField addressField;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_cancel;

//    public void loadServiceDetail(int serviceId) {
//        String query = "SELECT * FROM service_item WHERE id = ?";
//
//        try (Connection connection = MySQLConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, serviceId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                nameField.setText(resultSet.getString("name"));
//                priceField.setText(String.valueOf(resultSet.getDouble("price")));
//                unitField.setText(resultSet.getString("unit"));
//                quantityField.setText(String.valueOf(resultSet.getInt("quantity")));
//                descriptionField.setText(resultSet.getString("description"));
//                dosageField.setText(resultSet.getString("dosage"));
//                instructionField.setText(resultSet.getString("usage_instruction"));
//                addressField.setText(resultSet.getString("address"));  // Nếu có trường địa chỉ trong bảng.
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            MySQLConnection.closeConnection();
//        }
//    }

    private Service service;
    private Stage dialogStage;

    public void initialize(URL location, ResourceBundle resources) {
        btn_edit.setOnAction(event -> {

        });
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setService(Service service) {
        this.service = service;


    }


}
