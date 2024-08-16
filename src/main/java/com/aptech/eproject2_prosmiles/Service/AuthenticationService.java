package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;

public class AuthenticationService {

    /*LOGIN*/
    public static boolean login(Staff staff) {
        //check valid
        return true;
    }

    /*REGISTER*/
    public static boolean register(Staff staff) {
//        insert DB
        return true;
    }

    /*LOGOUT*/
    public static void logout() {
        AppProperties.setProperty("user.loggedin", "false");
        AppProperties.setProperty("user.username", "");
        AppProperties.setProperty("user.userrole", "");
        AppProperties.setProperty("user.userid", "");
    }
}
