package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class Prescription {
    private int id;
    private Patient patient;
    private Staff staff;
    private String description;
    private EStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EIsDeleted isDeleted;
    public Prescription() {;}

    public Prescription(int id, Patient patient, Staff staff, String description, EStatus status
            , LocalDateTime createdAt, LocalDateTime updatedAt, EIsDeleted isDeleted) {
        this.id = id;
        this.patient = patient;
        this.staff = staff;
        this.description = description;
        this.status = status;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Optional<Patient> patient) {
        this.patient = patient.orElse(null);
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Optional<Staff> staff) {
        this.staff = staff.orElse(null);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
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
        return "Prescription{" +
                "id=" + id +
                ", patient=" + patient +
                ", staff=" + staff +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
