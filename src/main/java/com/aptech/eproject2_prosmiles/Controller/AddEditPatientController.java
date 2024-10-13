package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Global.Validation;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Repository.PatientDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEditPatientController extends BaseController{
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_save;
    @FXML
    private RadioButton rd_female;
    @FXML
    private RadioButton rd_male;
    @FXML
    private RadioButton rd_other;
    @FXML
    private TextField txt_address;
    @FXML
    private TextField txt_age;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_phone;
    @FXML
    private Label lbl_inform;

    private ToggleGroup genderGroup;

    private Stage dialogStage;
    private boolean isEditMode = false;
    private boolean saved;
    private Patient patient;
    PatientDAO patientDAO = new PatientDAO();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean getIsSaved() {
        return saved;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderGroup = new ToggleGroup();
        rd_male.setToggleGroup(genderGroup);
        rd_female.setToggleGroup(genderGroup);
        rd_other.setToggleGroup(genderGroup);

        btn_save.setOnAction(this::handleSave);
        btn_cancel.setOnAction(event -> dialogStage.close());
    }

    public void initializeForm(){
        if(patient != null){
            txt_name.setText(patient.getName());
           EGender gender = patient.getGender();
            if (gender == EGender.MALE) {
                rd_male.setSelected(true);
            } else if (gender == EGender.FEMALE) {
                rd_female.setSelected(true);
            } else if (gender == EGender.OTHER) {
                rd_other.setSelected(true);
            }
            txt_phone.setText(patient.getPhone());
            txt_email.setText(patient.getEmail());
            txt_age.setText(String.valueOf(patient.getAge()));
            txt_address.setText(patient.getAddress());
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        try{
            String patientName = txt_name.getText();
            if(patientName == null || patientName.isEmpty()){
                throw new Exception("Patient name cannot be empty");
            }
            patient.setName(patientName);

            RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
            if (selectedRadioButton == null) {
                throw new Exception("Please select a gender");
            }

            String selectedGender = selectedRadioButton.getText();
            if ("Male".equalsIgnoreCase(selectedGender)) {
                patient.setGender(EGender.MALE);
            } else if ("Female".equalsIgnoreCase(selectedGender)) {
                patient.setGender(EGender.FEMALE);
            } else if ("Other".equalsIgnoreCase(selectedGender)) {
                patient.setGender(EGender.OTHER);
            }

            String phone = txt_phone.getText();
            if(phone == null || phone.isEmpty()){
                throw new Exception("Phone number cannot be empty");
            }
            if (Validation.isPhoneNumberValid(phone)) {
                patient.setPhone(phone);
            }else{
                throw new Exception("Phone number is invalid");
            }


            String email = txt_email.getText();
            if(email == null || email.isEmpty()){
                throw new Exception("Email cannot be empty");
            }
            if (Validation.isEmailValid(email)) {
                patient.setEmail(email);
            }else{
                throw new Exception("Email is invalid");
            }


            String age = txt_age.getText();
            if(age == null || age.isEmpty()){
                throw new Exception("Age cannot be empty");
            }
            patient.setAge(Integer.parseInt(age));

            String address = txt_address.getText();
            if(address == null || address.isEmpty()){
                throw new Exception("Address cannot be empty");
            }
            patient.setAddress(address);

            if(isEditMode){
                DialogHelper.showNotificationDialog("Edit Success", "Patient Edited Successfully");
            }else{
                patientDAO.save(patient);
                DialogHelper.showNotificationDialog("Add Success", "Patient Added Successfully");
            }

            saved = true;
            dialogStage.close();
        }catch(Exception e){
            e.printStackTrace();
            lbl_inform.setText(e.getMessage());
            lbl_inform.setStyle("-fx-text-fill: red");
        }
    }


}
