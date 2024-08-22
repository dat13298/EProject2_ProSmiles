package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EStatus {
    PENDING("Pending"), COMPLETED("Completed"), REJECT("Reject");
    private String status;
    EStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
