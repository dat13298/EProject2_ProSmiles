package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;

import java.time.LocalDateTime;
import java.util.Optional;

public class PrescriptionDetail {
    private int id;
    private Optional<ServiceItem> serviceItem;
    private Optional<Prescription> prescription;
    private String unit;
    private int quantity;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EIsDeleted isDeleted;
    public PrescriptionDetail() {;}

    public PrescriptionDetail(int id, Optional<ServiceItem> serviceItem, Optional<Prescription> prescription, String unit
            , int quantity, double price, LocalDateTime createdAt, LocalDateTime updatedAt, EIsDeleted isDeleted) {
        this.id = id;
        this.serviceItem = serviceItem;
        this.prescription = prescription;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
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

    public Optional<ServiceItem> getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(Optional<ServiceItem> serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Optional<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(Optional<Prescription> prescription) {
        this.prescription = prescription;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        return "PrescriptionDetail{" +
                "id=" + id +
                ", serviceItem=" + serviceItem +
                ", prescription=" + prescription +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
