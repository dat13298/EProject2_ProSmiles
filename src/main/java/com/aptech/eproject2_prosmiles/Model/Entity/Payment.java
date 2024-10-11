package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EPaymentType;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class Payment {
    private int id;
    private Prescription prescription;
    private String billNumber;
    private EPaymentType paymentType;
    private double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EIsDeleted isDeleted;
    public Payment() {;}

    public Payment(int id, Prescription prescription, String billNumber, EPaymentType paymentType
            , double totalAmount , LocalDateTime createdAt, LocalDateTime updatedAt, EIsDeleted isDeleted) {
        this.id = id;
        this.prescription = prescription;
        this.billNumber = billNumber;
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Optional<Prescription> prescription) {
        this.prescription = prescription.orElse(null);
    }

    public void setPrescription(Prescription prescription) {this.prescription = prescription;}

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public EPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EIsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(EIsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", prescription=" + prescription +
                ", billNumber='" + billNumber + '\'' +
                ", paymentType=" + paymentType +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
