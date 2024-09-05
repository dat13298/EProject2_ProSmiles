package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalInt;

public class ServiceItem {
    private int id;
    private Optional<Service> service;
    private String name;
    private double price;
    private String unit;
    private int quantity;
    private String description;
    private String dosage;
    private String usageInstruction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EIsDeleted isDeleted;
    public ServiceItem() {;}

    public ServiceItem(int id, Optional<Service> service, String name, double price
            , String unit, int quantity, String description, String dosage, String usageInstruction
            , LocalDateTime createdAt, LocalDateTime updatedAt, EIsDeleted isDeleted) {
        this.id = id;
        this.service = service;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.description = description;
        this.dosage = dosage;
        this.usageInstruction = usageInstruction;
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

    public Optional<Service> getService() {
        return service;
    }

    public void setService(Optional<Service> service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUsageInstruction() {
        return usageInstruction;
    }

    public void setUsageInstruction(String usageInstruction) {
        this.usageInstruction = usageInstruction;
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
        return "ServiceItem{" +
                "id=" + id +
                ", service=" + service +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", dosage='" + dosage + '\'' +
                ", usageInstruction='" + usageInstruction + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
