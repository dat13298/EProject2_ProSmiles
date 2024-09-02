package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EIsDeleted {
    ACTIVE(0), INACTIVE(1);
    private int value;
    EIsDeleted(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static EIsDeleted fromInt(int value) {
        for (EIsDeleted status : EIsDeleted.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }
}
