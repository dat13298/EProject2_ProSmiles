package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EStatus {
    COMPLETED("Completed"), PENDING("Pending"), Reject("Reject");
    private String status;
    EStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
