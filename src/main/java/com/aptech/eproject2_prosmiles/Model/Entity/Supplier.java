package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EItemType;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class Supplier extends Item {
    private String brand;
    private String email;
    private String description;
    private String phone;
    private Staff createdBy;
    private Staff updatedBy;
    private Staff deletedBy;

    public Supplier(int id, String name, Category category, LocalDateTime createAt, LocalDateTime updateAt, String brand, String email
            , String description, String phone, Staff createdBy, Staff updatedBy, Staff deletedBy) {
        super(id, name, category, EItemType.SUPPLIER, createAt, updateAt, IsDeleted.ACTIVE);
        this.brand = brand;
        this.email = email;
        this.description = description;
        this.phone = phone;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Staff getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Staff deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "brand='" + brand + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", deletedBy=" + deletedBy +
                '}';
    }
}
