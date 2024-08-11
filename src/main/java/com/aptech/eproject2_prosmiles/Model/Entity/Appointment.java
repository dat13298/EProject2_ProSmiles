package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Category category;
    private Staff createdBy;
    private Staff doctor;
    private LocalDateTime date;
    private Staff updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private IsDeleted isDeleted;

    public Appointment() {;}

    public Appointment(int id, Category category, Staff createdBy, Staff doctor, LocalDateTime date, Staff updatedBy
            , LocalDateTime createdDate, LocalDateTime updatedDate, IsDeleted isDeleted) {
        this.id = id;
        this.category = category;
        this.createdBy = createdBy;
        this.doctor = doctor;
        this.date = date;
        this.updatedBy = updatedBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Staff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
    }

    public Staff getDoctor() {
        return doctor;
    }

    public void setDoctor(Staff doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Staff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Staff updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public IsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", category=" + category +
                ", createdBy=" + createdBy +
                ", doctor=" + doctor +
                ", date=" + date +
                ", updatedBy=" + updatedBy +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
