package com.aptech.eproject2_prosmiles.Model.Enum;

public enum InventoryType {
    MEDICINE("Medicine"), EQUIPMENT("Equipment");
    private String value;
    InventoryType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
