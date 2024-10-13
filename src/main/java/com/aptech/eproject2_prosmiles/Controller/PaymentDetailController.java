package com.aptech.eproject2_prosmiles.Controller;


import com.aptech.eproject2_prosmiles.Global.DialogHelper;
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
    private Button btnCancel;
    @FXML
    private Button btnEdit;
    @FXML
    private Label lblAmount;
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblPaymentNumber;
    @FXML
    private Label lblPaymentType;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btn_payment_export;


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

        lblPatientName.setText(paymentClicked.getPrescription().getPatient().getName());
        lblPaymentNumber.setText(paymentClicked.getBillNumber());
        lblAmount.setText(String.valueOf(paymentClicked.getTotalAmount()));
        lblPaymentType.setText(paymentClicked.getPaymentType().getValue());
        lblStatus.setText(paymentClicked.getPrescription().getStatus().getStatus());

        Prescription prescription = prescriptionDAO.getById(paymentClicked.getPrescription().getId());

        paymentClicked.setPrescription(prescription);
        prescriptionDetails = prescriptionDetailDAO.getPresDetailByPresId(paymentClicked.getPrescription().getId());

        // Loop through each prescription detail and fetch the service using the serviceItem ID
        for (PrescriptionDetail detail : prescriptionDetails) {
            if (detail.getService() != null) {
                // Fetch the service by the serviceItem's ID
                Service service = serviceDAO.getById(detail.getService().getId());

                // Set the fetched service back to the prescription detail's serviceItem
                detail.setService(service);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEdit.setOnAction(event -> {
            paymentListController.showAddEditPayment(payment);
        });
        btnCancel.setOnAction(event -> dialogStage.close());
        btn_payment_export.setOnMouseClicked(event -> {
            exportToPDF("PaymentDetails.pdf");
        });
    }

    public void exportToPDF(String destinationFileName) {
        try {
            System.out.println(prescriptionDetails);
            // Construct the path to the "billing" folder from the content root
            String billingFolder = "billing";
            File folder = new File(billingFolder);

            // Create the "billing" directory if it doesn't exist
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Full path to the PDF file in the billing folder
            String fullPath = billingFolder + File.separator + destinationFileName;

            // Initialize PDF writer with the full path
            PdfWriter writer = new PdfWriter(fullPath);

            // Initialize PDF document
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // Add Invoice Header
            document.add(new Paragraph("PAYMENT DETAIL")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Add line separator
            LineSeparator ls = new LineSeparator(new SolidLine());
            document.add(ls);

            // Add company info
            document.add(new Paragraph("ProSmiles Dental")
                    .setBold()
                    .setFontSize(12)
                    .setMarginBottom(5));
            document.add(new Paragraph("285 Doi Can, Ba Dinh , Ha Noi")
                    .setFontSize(10));
            document.add(new Paragraph("Email: ProSmiles@pro.com | Phone: (123) 456-7890")
                    .setFontSize(10)
                    .setMarginBottom(20));

            // Add date and payment number
            document.add(new Paragraph("Invoice Date: " + java.time.LocalDate.now().toString())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Payment Number: " + lblPaymentNumber.getText())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));

            // Add patient and payment details
            document.add(new Paragraph("\nBill To:")
                    .setBold()
                    .setFontSize(12)
                    .setMarginBottom(5));
            document.add(new Paragraph("Patient Name: " + lblPatientName.getText())
                    .setFontSize(10));
            document.add(new Paragraph("Payment Type: " + lblPaymentType.getText())
                    .setFontSize(10)
                    .setMarginBottom(5));

            // Add line separator
            document.add(new LineSeparator(new SolidLine()));

            // Create table for payment breakdown
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginTop(20);

            // Table Header
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

            // Dummy data for the table (you can use your actual data)
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

            // Add table to document
            document.add(table);


            // Add total amount
            document.add(new Paragraph("\nTotal Amount: $" + lblAmount.getText())
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT));

            // Footer with thank you message
            document.add(new Paragraph("\nThank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50)
                    .setFontSize(12)
                    .setItalic());

            // Close document
            document.close();

            DialogHelper.showNotificationDialog("Export Success", "Successfully exported payment details");

            System.out.println("PDF created successfully at: " + fullPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
