package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    /*CHECK USER FROM FILE SAVED*/
    public static boolean authenticateFromFile(Properties properties) {
        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Documents/Aptech/EProject2_ProSmiles/resources/application.properties";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
//        authenticate account
        if(Boolean.parseBoolean(properties.getProperty("user.isremember"))) {
            return true;
        }
        return false;
    }
}
