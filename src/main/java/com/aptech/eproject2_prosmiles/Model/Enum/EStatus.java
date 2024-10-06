package com.aptech.eproject2_prosmiles.Model.Enum;

import java.util.Objects;

public enum EStatus {
    PENDING("Pending"), COMPLETED("Completed"), REJECT("Reject");
    private String status;
    EStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public static EStatus fromString(String value) {
        for (EStatus status : EStatus.values()) {
            if (Objects.equals(status.getStatus(), value)) {
                return status;
            }
        }
        return null;
    }

}
