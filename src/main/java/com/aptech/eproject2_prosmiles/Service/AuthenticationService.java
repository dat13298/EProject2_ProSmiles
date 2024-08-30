package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthenticationService {

    /*LOGIN*/
    public static boolean login(Staff staff) {
        //check valid
        Staff staffLogged = StaffDAO.validateLogin(staff);
        return true;
    }

    /*REGISTER*/
    public static boolean register(Staff staff) {
//        insert DB
        return true;
    }

    /*LOGOUT*/
    public static void logout() {
        AppProperties.setProperty("staff.loggedin", "false");
        AppProperties.setProperty("staff.username", "");
        AppProperties.setProperty("staff.userrole", "");
        AppProperties.setProperty("staff.userid", "");
    }

    /*CHECK USER FROM FILE SAVED*/
    public static boolean authenticateFromFile(Properties properties) {
        String userHome = System.getProperty("staff.home");
        String filePath = userHome + "/application.properties";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
//        authenticate account
        if(Boolean.parseBoolean(properties.getProperty("staff.isremember"))) {
            return true;
        }
        return false;
    }
}
