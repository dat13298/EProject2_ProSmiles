package com.aptech.eproject2_prosmiles.Model.Enum;

public enum IsDeleted {
    ACTIVE(0), INACTIVE(1);
    private int value;
    IsDeleted(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
