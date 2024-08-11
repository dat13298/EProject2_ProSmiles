package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EItemType;
import com.aptech.eproject2_prosmiles.Model.Enum.InventoryType;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class Inventory extends Item {
    private Supplier supplier;
    private String unit;
    private InventoryType inventoryType;
    private int quantity;
    private double purchasePrice;
    private double salePrice;
    private String description;
    private Staff created_by;
    private Staff updated_by;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Inventory(int id, String name, Category category, EItemType itemType, LocalDateTime createAt
            , LocalDateTime updateAt, IsDeleted itemDeleted, Supplier supplier, String unit, InventoryType inventoryType
            , int quantity, double purchasePrice, double salePrice, String description, Staff created_by
            , Staff updated_by, LocalDateTime created_at, LocalDateTime updated_at) {
        super(id, name, category, itemType, createAt, updateAt, itemDeleted);
        this.supplier = supplier;
        this.unit = unit;
        this.inventoryType = inventoryType;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.description = description;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Staff getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Staff created_by) {
        this.created_by = created_by;
    }

    public Staff getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Staff updated_by) {
        this.updated_by = updated_by;
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

    @Override
    public String toString() {
        return "Inventory{" +
                "supplier=" + supplier +
                ", unit='" + unit + '\'' +
                ", inventoryType=" + inventoryType +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", salePrice=" + salePrice +
                ", description='" + description + '\'' +
                ", created_by=" + created_by +
                ", updated_by=" + updated_by +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
