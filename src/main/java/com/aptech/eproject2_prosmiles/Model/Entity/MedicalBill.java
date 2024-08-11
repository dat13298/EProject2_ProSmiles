package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;
import javafx.scene.Parent;

import java.time.LocalDateTime;

public class MedicalBill {
    private int id;
    private Category category;
    private Patient patient;
    private Staff receptionist;
    private String diagnose;
    private Staff doctor;
    private Staff updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EStatus status;
    private IsDeleted isDeleted;

    public MedicalBill() {;}

    public MedicalBill(int id, Category category, Patient patient, Staff receptionist, String diagnose, Staff doctor
            , Staff updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, EStatus status, IsDeleted isDeleted) {
        this.id = id;
        this.category = category;
        this.patient = patient;
        this.receptionist = receptionist;
        this.diagnose = diagnose;
        this.doctor = doctor;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Staff receptionist) {
        this.receptionist = receptionist;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public Staff getDoctor() {
        return doctor;
    }

    public void setDoctor(Staff doctor) {
        this.doctor = doctor;
    }

    public Staff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Staff updatedBy) {
        this.updatedBy = updatedBy;
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

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public IsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "MedicalBill{" +
                "id=" + id +
                ", category=" + category +
                ", patient=" + patient +
                ", receptionist=" + receptionist +
                ", diagnose='" + diagnose + '\'' +
                ", doctor=" + doctor +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
