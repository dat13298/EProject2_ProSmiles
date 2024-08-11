package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EItemType;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class MedicalBillDetail {
    private int id;
    private MedicalBill medicalBill;
    private Item item;
    private int quantity;
    private EItemType type;
    private Staff createdBy;
    private Staff updatedBy;
    private double total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private IsDeleted isDeleted;

    public MedicalBillDetail() {;}
    public MedicalBillDetail(int id, MedicalBill medicalBill, Item item, int quantity, EItemType type, Staff createdBy
            , Staff updatedBy, double total, LocalDateTime createdAt, LocalDateTime updatedAt, IsDeleted isDeleted) {
        this.id = id;
        this.medicalBill = medicalBill;
        this.item = item;
        this.quantity = quantity;
        this.type = type;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.total = total;
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

    public MedicalBill getMedicalBill() {
        return medicalBill;
    }

    public void setMedicalBill(MedicalBill medicalBill) {
        this.medicalBill = medicalBill;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EItemType getType() {
        return type;
    }

    public void setType(EItemType type) {
        this.type = type;
    }

    public Staff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
    }

    public Staff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Staff updatedBy) {
        this.updatedBy = updatedBy;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public IsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "MedicalBillDetail{" +
                "id=" + id +
                ", medicalBill=" + medicalBill +
                ", item=" + item +
                ", quantity=" + quantity +
                ", type=" + type +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
