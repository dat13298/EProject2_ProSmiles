package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.RoleDAO;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class AuthenticationService {

    private static final StaffDAO staffDAO = new StaffDAO();
    private static final RoleDAO roleDAO = new RoleDAO();

    /*LOGIN*/
    public static boolean login(Staff staff) {
        System.out.println("Password to login with: " + staff.getPassword());//admin123

        Staff staffFromDb = StaffDAO.getStaffByPhoneOrEmail(staff);

        System.out.println("Password found in database: " + staffFromDb.getPassword());

        // check password
        boolean passwordMatch = BCrypt.checkpw(staff.getPassword(), staffFromDb.getPassword());//false
        System.out.println("Password is match: " + passwordMatch);

        if (passwordMatch) {
            System.out.println("LOGIN SUCCESS");
            updateAppPropertiesWithStaffInfo(staffFromDb);
            return true;
        }

        System.out.println("LOGIN FAILED: Incorrect password");
        return false;
    }

    /* Register */
    public static boolean register(Staff staff) {
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

    /* Log out */
    public static void logout() {
        System.out.println("Logging out...");
        AppProperties.setProperty("staff.loggedin", "false");
        AppProperties.setProperty("staff.staffid", "");
        AppProperties.setProperty("staff.roleid", "");
        AppProperties.setProperty("staff.name", "");
        AppProperties.setProperty("staff.phone", "");
        AppProperties.setProperty("staff.username", "");
        AppProperties.setProperty("staff.userrole", "");
        AppProperties.setProperty("staff.userid", "");
    }

    /* check staff remember */
    public static boolean authenticateFromFile(Properties properties) {
        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/application.properties";

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error during authentication from file: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // validate staff from app.properties
        return Boolean.parseBoolean(properties.getProperty("staff.isremember"));
    }

    /* update app.properties when logged*/
    private static void updateAppPropertiesWithStaffInfo(Staff staff) {
        System.out.println("Updating AppProperties with staff information...");
        AppProperties.setProperty("staff.loggedin", "true");
        AppProperties.setProperty("staff.staffid", String.valueOf(staff.getId()));
        AppProperties.setProperty("staff.roletitle", String.valueOf(roleDAO.getById(staff.getRole().getId())));
        AppProperties.setProperty("staff.name", staff.getLastName());
        AppProperties.setProperty("staff.gender", staff.getEGender().getGender());
        AppProperties.setProperty("staff.phone", staff.getPhone());
        AppProperties.setProperty("staff.password", staff.getPassword());
        AppProperties.setProperty("staff.address", staff.getAddress());
        AppProperties.setProperty("staff.email", staff.getEmail());
        AppProperties.setProperty("staff.age", String.valueOf(staff.getAge()));
        AppProperties.setProperty("staff.imagepath", staff.getImagePath());
        AppProperties.setProperty("staff.createat", Format.formatDate(staff.getCreatedAt()));
        AppProperties.setProperty("staff.updateat", Format.formatDate(staff.getUpdatedAt()));
    }
}
