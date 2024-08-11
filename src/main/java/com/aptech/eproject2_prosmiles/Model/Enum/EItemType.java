package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EItemType {
    INVENTORY("Inventory"), SERVICE("Servicec"), STAFF("Staff"), SUPPLIER("Supplier");
    private String value;
    EItemType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
