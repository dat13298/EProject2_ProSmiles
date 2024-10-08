package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.RoleDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StaffListController extends BaseController {
    @FXML private TableView<Staff> tblStaff;
    @FXML private TableColumn<Staff, Integer> c_id;
    @FXML private TableColumn<Staff, String> c_name;
    @FXML private TableColumn<Staff, String> c_gender;
    @FXML private TableColumn<Staff, String> c_role;
    @FXML private TableColumn<Staff, String> c_phone;
    @FXML private TableColumn<Staff, String> c_email;
    @FXML private TableColumn<Staff, Integer> c_age;

    private ObservableList<Staff> staffList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StaffDAO staffDAO = new StaffDAO();
        RoleDAO roleDAO = new RoleDAO();

        staffList = staffDAO.getAll();
        tblStaff.setEditable(true);
//        tblStaff.getColumns().get(0).setVisible(false);
        c_id.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getId();
            return new SimpleObjectProperty<>(id);
        });

        c_name.setCellValueFactory(cellData -> {
            Staff staff = cellData.getValue();
            String fullName = staff.getFirstName() + " " + staff.getLastName();
            return new SimpleStringProperty(fullName);
        });
        c_gender.setCellValueFactory(cellData -> {
            Staff staff = cellData.getValue();
            String gender = staff.getEGender().getGender();
            return new SimpleStringProperty(gender);
        });
        c_role.setCellValueFactory(cellData -> {
            Staff staff = cellData.getValue();
            Role role = roleDAO.getById(staff.getRole().getId());
            staff.setRole(role);
            String roleTitle = staff.getRole().getTitle();
            return new SimpleStringProperty(roleTitle);
        });
        c_phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        c_email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        c_age.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAge()));
        tblStaff.setItems(staffList);

        tblStaff.setRowFactory(tv -> {
            TableRow<Staff> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Staff staffClicked = row.getItem();
                    showStaffDetail(staffClicked);
                }
            });
            return row;
        });
    }

    private void showStaffDetail(Staff staffClicked) {
        try {
            InputStream fxmlStream = getClass().getResourceAsStream("/com/aptech/eproject2_prosmiles/View/StaffManager/StaffDetail.fxml");
            if (fxmlStream == null) {
                System.err.println("FXML file not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(fxmlStream);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Staff Detail");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogStage.setScene(scene);

            StaffDetailController detailController = loader.getController();
            detailController.setStaffDetails(staffClicked);
            detailController.setDialogStage(dialogStage);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
