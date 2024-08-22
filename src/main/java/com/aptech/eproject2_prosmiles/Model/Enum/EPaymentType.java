package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EPaymentType {
    CASH("Cash"), TRANSFER("Transfer");
    private String value;
    EPaymentType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
