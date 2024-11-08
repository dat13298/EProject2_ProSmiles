package com.aptech.eproject2_prosmiles.Controller;


import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDAO;
import com.aptech.eproject2_prosmiles.Repository.PrescriptionDetailDAO;
import com.aptech.eproject2_prosmiles.Repository.ServiceDAO;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentDetailController extends BaseController{
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_edit;
    @FXML
    private Label lbl_amount;
    @FXML
    private Label lbl_patient_name;
    @FXML
    private Label lbl_payment_number;
    @FXML
    private Label lbl_payment_type;
    @FXML
    private Label lbl_status;
    @FXML
    private Button btn_payment_export;

    private MethodInterceptor methodInterceptor;


    private Payment payment;
    private Stage dialogStage;
    private PaymentListController paymentListController;
    private ObservableList<PrescriptionDetail> prescriptionDetails;

    public void setPaymentListController(PaymentListController paymentListController) {
        this.paymentListController = paymentListController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPaymentDetail(Payment paymentClicked) {
        this.payment = paymentClicked;

        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();

        lbl_patient_name.setText(paymentClicked.getPrescription().getPatient().getName());
        lbl_payment_number.setText(paymentClicked.getBillNumber());
        lbl_amount.setText(String.valueOf(paymentClicked.getTotalAmount()));
        lbl_payment_type.setText(paymentClicked.getPaymentType().getValue());
        lbl_status.setText(paymentClicked.getPrescription().getStatus().getStatus());

        Prescription prescription = prescriptionDAO.getById(paymentClicked.getPrescription().getId());

        paymentClicked.setPrescription(prescription);
        prescriptionDetails = prescriptionDetailDAO.getPresDetailByPresId(paymentClicked.getPrescription().getId());

        for (PrescriptionDetail detail : prescriptionDetails) {
            if (detail.getService() != null) {
                Service service = serviceDAO.getById(detail.getService().getId());
                detail.setService(service);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodInterceptor = new MethodInterceptor(this);
        btn_edit.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleEdit", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        btn_cancel.setOnAction(event -> dialogStage.close());
        btn_payment_export.setOnAction((ActionEvent event) -> {
            try {
                methodInterceptor.invokeMethod("handleExport", event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @RolePermissionRequired(roles = {"Manager", "Receptionist"})
    public void handleEdit(ActionEvent event) {
        paymentListController.showAddEditPayment(payment);
    }

    @RolePermissionRequired(roles = {"Manager", "Receptionist"})
    public void handleExport(ActionEvent event) {
        exportToPDF("PaymentDetails_" + lbl_payment_number.getText() + ".pdf");
    }

    public void exportToPDF(String destinationFileName) {
        try {
            System.out.println(prescriptionDetails);

            String billingFolder = "billing";
            File folder = new File(billingFolder);


            if (!folder.exists()) {
                folder.mkdirs();
            }


            String fullPath = billingFolder + File.separator + destinationFileName;

            PdfWriter writer = new PdfWriter(fullPath);

            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            document.add(new Paragraph("PAYMENT DETAIL")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            LineSeparator ls = new LineSeparator(new SolidLine());
            document.add(ls);

            document.add(new Paragraph("ProSmiles Dental")
                    .setBold()
                    .setFontSize(12)
                    .setMarginBottom(5));
            document.add(new Paragraph("285 Doi Can, Ba Dinh , Ha Noi")
                    .setFontSize(10));
            document.add(new Paragraph("Email: ProSmiles@pro.com | Phone: (123) 456-7890")
                    .setFontSize(10)
                    .setMarginBottom(20));

            document.add(new Paragraph("Invoice Date: " + java.time.LocalDate.now().toString())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Payment Number: " + lbl_payment_number.getText())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("\nBill To:")
                    .setBold()
                    .setFontSize(12)
                    .setMarginBottom(5));
            document.add(new Paragraph("Patient Name: " + lbl_patient_name.getText())
                    .setFontSize(10));
            document.add(new Paragraph("Payment Type: " + lbl_payment_type.getText())
                    .setFontSize(10)
                    .setMarginBottom(5));

            document.add(new LineSeparator(new SolidLine()));

            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginTop(20);

            Color tableHeaderColor = new DeviceRgb(63, 169, 219); // Customize your header color
            Cell header1 = new Cell().add(new Paragraph("Service Name"))
                    .setBackgroundColor(tableHeaderColor)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            Cell header2 = new Cell().add(new Paragraph("Unit"))
                    .setBackgroundColor(tableHeaderColor)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            Cell header3 = new Cell().add(new Paragraph("Quantity"))
                    .setBackgroundColor(tableHeaderColor)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            Cell header4 = new Cell().add(new Paragraph("Price"))
                    .setBackgroundColor(tableHeaderColor)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(header1);
            table.addHeaderCell(header2);
            table.addHeaderCell(header3);
            table.addHeaderCell(header4);

            for (PrescriptionDetail detail : prescriptionDetails) {
                table.addCell(new Cell().add(new Paragraph(detail.getService().getName()))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setBorder(new SolidBorder(1)));
                table.addCell(new Cell().add(new Paragraph(detail.getUnit()))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(new SolidBorder(1)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getQuantity())))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(new SolidBorder(1)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getPrice())))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(new SolidBorder(1)));
            }


            document.add(table);


            document.add(new Paragraph("\nTotal Amount: $" + lbl_amount.getText())
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("\nThank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50)
                    .setFontSize(12)
                    .setItalic());

            document.close();

            DialogHelper.showNotificationDialog("Export Success", "Successfully exported payment details");

            System.out.println("PDF created successfully at: " + fullPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
