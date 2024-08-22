package com.aptech.eproject2_prosmiles.Model.Enum;

public enum EGender {
    MALE("Male"), FEMALE("Female"), OTHER("Other");
    private String gender;
    EGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }
}
