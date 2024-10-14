package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDetailDAO;
import com.aptech.eproject2_prosmiles.Repository.ServiceDAO;
import com.aptech.eproject2_prosmiles.Repository.ServiceItemDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddNewPrescriptionInfoController extends BaseController{
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private ComboBox<Service> cmb_service;

    @FXML
    private Label lbl_inform;

    @FXML
    private TextField txt_quantity;

    @FXML
    private TextField txt_unit;

    private Stage dialogStage;
    private boolean isEditMode;
    private boolean saved = false;

    private PrescriptionDetail prescriptionDetail;
    PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();



    public void setPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
        this.prescriptionDetail = prescriptionDetail;
    }

    public boolean getIsSaved() {
        return saved;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ServiceDAO serviceDAO = new ServiceDAO();
        ObservableList<Service> services = serviceDAO.getAll();
        cmb_service.getItems().clear();
        cmb_service.getItems().addAll(services);

        btn_save.setOnAction(this::handleSave);
        btn_cancel.setOnAction(event -> dialogStage.close());
    }


    public void initializeForm(){
        if(prescriptionDetail != null){
            if(prescriptionDetail.getService() != null){
                cmb_service.getSelectionModel().select(prescriptionDetail.getService());
            } else{
                cmb_service.getSelectionModel().select(null);
            }
            if(prescriptionDetail.getUnit() != null){
                txt_unit.setText(prescriptionDetail.getUnit());
            }else {
                txt_unit.clear();
            }
            if(prescriptionDetail.getQuantity() != 0){
                txt_quantity.setText(String.valueOf(prescriptionDetail.getQuantity()));
            }else{
                txt_quantity.clear();
            }
        }
    }

    private void handleSave(ActionEvent actionEvent) {
        try{
            Service service = cmb_service.getSelectionModel().getSelectedItem();
            if(service == null){
                throw new Exception("Please Choose a Service ");
            }
            prescriptionDetail.setService(service);


            int quantity = Integer.parseInt(txt_quantity.getText().trim());
            if(quantity == 0){
                throw new Exception("Please Enter Quantity");
            }
            prescriptionDetail.setQuantity(quantity);

            String unit = txt_unit.getText();
            if(unit == null || unit.equals("")){
                throw new Exception("Please Enter Unit");
            }
            prescriptionDetail.setUnit(unit);

            //Set Price ...
            double total = caculateTotalPrice(prescriptionDetail);
            prescriptionDetail.setPrice(total);


            if(isEditMode){
                DialogHelper.showNotificationDialog("Edit Success", "Successfully Updated");
            }else{
                prescriptionDetailDAO.save(prescriptionDetail);
                DialogHelper.showNotificationDialog("Add Success", "Successfully Added");
            }

            saved = true;
            dialogStage.close();
        }catch (Exception e){
            e.printStackTrace();
            lbl_inform.setText(e.getMessage());
            lbl_inform.setStyle("-fx-text-fill: red");

        }
    }

    public double caculateTotalPrice(PrescriptionDetail prescriptionDetail){
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
        ObservableList<ServiceItem> serviceItems = serviceItemDAO.getAll();
        double sumItems = serviceItems.stream()
                .filter(item -> item.getService().getId() == prescriptionDetail.getService().getId())
                .mapToDouble(ServiceItem::getPrice)
                .sum();
        double totalPrice = sumItems * prescriptionDetail.getQuantity();
        return totalPrice;
    }


}
