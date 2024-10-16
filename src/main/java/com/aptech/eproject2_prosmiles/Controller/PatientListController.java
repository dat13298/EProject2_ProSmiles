package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import com.itextpdf.layout.element.Table;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PatientListController extends BaseController {
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_delete;
    @FXML
    private TableColumn<Patient, String> col_address;
    @FXML
    private TableColumn<Patient, String> col_gender;
    @FXML
    private TableColumn<Patient, Integer> col_id;
    @FXML
    private TableColumn<Patient, String> col_name;
    @FXML
    private TableColumn<Patient, String> col_phone;
    @FXML
    private TableView<Patient> tbl_patient;
    @FXML
    private TableColumn<Patient, Integer> col_age;

    private ObservableList<Patient> patientList;
    private PatientDetailController patientDetailController;
    private MethodInterceptor methodInterceptor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);

        PatientDAO patientDAO = new PatientDAO();

        patientList = patientDAO.getAll();
        tbl_patient.setEditable(true);

        col_id.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleObjectProperty<Integer>(patient.getId());
        });

        col_name.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleStringProperty(patient.getName());
        });

        col_gender.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleStringProperty(patient.getGender().getGender());
        });

        col_phone.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleStringProperty(patient.getPhone());
        });

        col_address.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleStringProperty(patient.getAddress());
        });

        col_age.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleObjectProperty<>(patient.getAge());
        });

        tbl_patient.setItems(patientList);

        tbl_patient.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    Patient patientClicked = row.getItem();
                    showPatientDetail(patientClicked);
                }
            });
            return row;
        });

        btn_add.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleAddPatient", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        btn_delete.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleDeletePatient", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RolePermissionRequired(roles = {"Manager", "Receptionist"})
    public void handleAddPatient(ActionEvent actionEvent) {
        Patient newPatient = new Patient();
        boolean isEditMode = false;
        showAddEditForm(newPatient, isEditMode);
    }

    @RolePermissionRequired(roles = {"Manager"})
    public void handleDeletePatient(ActionEvent actionEvent) {
        Patient selectedPatient = tbl_patient.getSelectionModel().getSelectedItem();
        if(selectedPatient != null) {
            boolean confirmed = DialogHelper.showConfirmationDialog("Confirm for delete", "Do you want to delete this patient?");
            if(confirmed) {
                selectedPatient.setIsDeleted(EIsDeleted.INACTIVE);
                PatientDAO patientDAO = new PatientDAO();
                patientDAO.delete(selectedPatient);
                tbl_patient.getItems().remove(selectedPatient);
                tbl_patient.refresh();
            }
        }
    }

    private void showPatientDetail(Patient patientClicked) {
        try{
            InputStream fxmlStream = getClass().getResourceAsStream("/com/aptech/eproject2_prosmiles/View/Patient/PatientDetail.fxml");
            if(fxmlStream == null) {
                System.err.println("FXML file not found");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(fxmlStream);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Patient Detail");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogStage.setScene(scene);

            PatientDetailController detailController = fxmlLoader.getController();
            patientDetailController = detailController;
            detailController.setPatient(patientClicked);
            detailController.setDialogStage(dialogStage);
            detailController.setPatientListController(this);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showAddEditForm(Patient patient, boolean isEditMode) {
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/aptech/eproject2_prosmiles/View/Patient/AddEditPatient.fxml")
            );
            Stage dialogStage = new Stage();
            PatientDAO patientDAO = new PatientDAO();

            dialogStage.setTitle(isEditMode ? "Edit Patient" : "Add Patient");

            dialogStage.initModality(Modality.WINDOW_MODAL);

            dialogStage.setScene(new Scene(loader.load()));
            AddEditPatientController controller = loader.getController();

            controller.setDialogStage(dialogStage);
            controller.setPatient(patient);
            controller.setEditMode(isEditMode);

            controller.initializeForm();

            dialogStage.showAndWait();

            if(controller.getIsSaved()){
                if(isEditMode){
                    patientDAO.update(patient);
                    patientDetailController.setPatient(patient);
                }
                patientList.clear();
                patientList = patientDAO.getAll();
                tbl_patient.setItems(patientList);
                tbl_patient.refresh();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
