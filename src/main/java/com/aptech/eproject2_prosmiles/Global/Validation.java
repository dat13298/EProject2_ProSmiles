package com.aptech.eproject2_prosmiles.Global;

public class Validation {
    public static boolean isPhoneNumberValid(String phoneNumber) {
        String pattern = "^[0-9]{10,15}";
        return phoneNumber.matches(pattern);
    }
    public static boolean isEmailValid(String email) {
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n";
//        example matches:
//        example@example.com
//        user.name@domain.co.uk
//        first_last123@sub.domain.org
        return email.matches(pattern);
    }
}
