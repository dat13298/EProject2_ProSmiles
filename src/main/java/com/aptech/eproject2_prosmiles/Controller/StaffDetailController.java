package com.aptech.eproject2_prosmiles.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Label lbl_title_change_password;
    @FXML
    private Label lbl_password_current;
    @FXML
    private TextField txt_password_current;
    @FXML
    private Label lbl_password_new;
    @FXML
    private TextField txt_password_new;
    @FXML
    private Button btn_change_password;
    @FXML
    private Label lbl_notification;
    @FXML
    private TableView<PatientHistory> tbl_patient_history;

    @FXML
    private TableColumn<PatientHistory, String> col_patient_name;

    @FXML
    private TableColumn<PatientHistory, String> col_appointment_date;

    @FXML
    private TableColumn<PatientHistory, String> col_doctor_name;

    @FXML
    private TableColumn<PatientHistory, String> col_diagnosis;

    @FXML
    private TableColumn<PatientHistory, String> col_treatment;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_patient_name.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        col_appointment_date.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        col_doctor_name.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        col_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        col_treatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));

        // Sample data
        ObservableList<PatientHistory> data = FXCollections.observableArrayList(
                new PatientHistory("Nguyen Van A", "2024-08-20", "Dr. Le Thi B", "Caries", "Filling"),
                new PatientHistory("Tran Thi C", "2024-08-21", "Dr. Nguyen Van D", "Gingivitis", "Cleaning"),
                new PatientHistory("Le Van E", "2024-08-22", "Dr. Pham Thi F", "Tooth Extraction", "Surgery"),
                new PatientHistory("Hoang Thi G", "2024-08-23", "Dr. Bui Van H", "Whitening", "Whitening Treatment"),
                new PatientHistory("Pham Van I", "2024-08-24", "Dr. Tran Thi J", "Orthodontics", "Braces")
        );

        tbl_patient_history.setItems(data);
    }

    // Inner class to represent the patient history data
    public static class PatientHistory {
        private final String patientName;
        private final String appointmentDate;
        private final String doctorName;
        private final String diagnosis;
        private final String treatment;

        public PatientHistory(String patientName, String appointmentDate, String doctorName, String diagnosis, String treatment) {
            this.patientName = patientName;
            this.appointmentDate = appointmentDate;
            this.doctorName = doctorName;
            this.diagnosis = diagnosis;
            this.treatment = treatment;
        }

        public String getPatientName() {
            return patientName;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public String getTreatment() {
            return treatment;
        }
    }
}
