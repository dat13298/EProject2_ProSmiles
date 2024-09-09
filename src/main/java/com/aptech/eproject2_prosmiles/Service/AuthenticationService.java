package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.RoleDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthenticationService {
    private static final StaffDAO staffDAO = new StaffDAO();
    private static final RoleDAO roleDAO = new RoleDAO();

    /*LOGIN*/
    public static boolean login(Staff staff) {
        //check valid
        String password = staff.getPassword();
        Staff staffLogged = StaffDAO.getStaffByPhoneOrEmail(staff);
        return BCrypt.checkpw(password, staffLogged.getPassword());
    }

    /*REGISTER*/
    public static boolean register(Staff staff) {
//        insert DB
        System.out.println("Attempting to register with: " + staff.getPassword());//password register
        String hashedPassword = BCrypt.hashpw(staff.getPassword(), BCrypt.gensalt());// string hashPassword
        System.out.println("Password hashed after register: " + hashedPassword);// print hash

        System.out.println();
        System.out.println("======test bcrypt checkpw======");
//        check is match password and hashed String
        System.out.println("Password is match after register: " + BCrypt.checkpw(staff.getPassword(), hashedPassword));//true
        System.out.println();

        staff.setPassword(hashedPassword);//set hash to password property
        staffDAO.save(staff);
        return true;
    }

    /*LOGOUT*/
    public static void logout() {
        AppProperties.setProperty("staff.loggedin", "false");
        AppProperties.setProperty("staff.userid", "");
        AppProperties.setProperty("staff.roleid", "");
        AppProperties.setProperty("staff.name", "");
        AppProperties.setProperty("staff.phone", "");
        AppProperties.setProperty("staff.username", "");
        AppProperties.setProperty("staff.userrole", "");
        AppProperties.setProperty("staff.userid", "");
    }

    /*CHECK USER FROM FILE SAVED*/
    public static boolean authenticateFromFile(Properties properties) {
        String userHome = System.getProperty("user.dir");
        String filePath = userHome + "/src/main/resources/application.properties";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
            System.out.println(properties.getProperty("staff.isremember"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        authenticate account
        if (Boolean.parseBoolean(properties.getProperty("staff.isremember"))) {
            return true;
        }
        return false;
    }
}
