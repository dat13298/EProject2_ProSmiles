package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class StockIn {
    private int id;
    private Supplier supplier;
    private Staff staff;
    private Category category;
    private String description;
    private Staff updatedBy;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private IsDeleted isDeleted;

    public StockIn() {;}

    public StockIn(int id, Supplier supplier, Staff staff, Category category, String description, Staff updatedBy
            , LocalDateTime created_at, LocalDateTime updated_at, IsDeleted isDeleted) {
        this.id = id;
        this.supplier = supplier;
        this.staff = staff;
        this.category = category;
        this.description = description;
        this.updatedBy = updatedBy;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Staff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Staff updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public IsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "StockIn{" +
                "id=" + id +
                ", supplier=" + supplier +
                ", staff=" + staff +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", updatedBy=" + updatedBy +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
