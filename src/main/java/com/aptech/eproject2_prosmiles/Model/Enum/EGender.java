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
    public static EGender fromValue(String value) {
        for (EGender e : EGender.values()) {
            if (e.gender.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
