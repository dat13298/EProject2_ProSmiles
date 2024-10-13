package com.aptech.eproject2_prosmiles.Service;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
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

        try {
            boolean isLoginSuccess = BCrypt.checkpw(password, staffLogged.getPassword());

            if (isLoginSuccess) {
                staffLogged = staffDAO.getById(staffLogged.getId());
                AppProperties.setProperty("staff.loggedin", "false");
                AppProperties.setProperty("staff.staffid", String.valueOf(staffLogged.getId()));
                Role roleOfStaffLogged = roleDAO.getById(staffLogged.getRole().getId());
                staffLogged.setRole(roleOfStaffLogged);
                AppProperties.setProperty("staff.roletitle", roleOfStaffLogged.getTitle());
                AppProperties.setProperty("staff.name", staffLogged.getFirstName() + " " + staffLogged.getLastName());
                AppProperties.setProperty("staff.gender", staffLogged.getEGender().getGender());
                AppProperties.setProperty("staff.phone", staffLogged.getPhone());
                AppProperties.setProperty("staff.password", staffLogged.getPassword());
                AppProperties.setProperty("staff.address", staffLogged.getAddress());
                AppProperties.setProperty("staff.email", staffLogged.getEmail());
                AppProperties.setProperty("staff.age", String.valueOf(staffLogged.getAge()));
                AppProperties.setProperty("staff.imagepath", staffLogged.getImagePath());
                AppProperties.setProperty("staff.createat", Format.formatDate(staffLogged.getCreatedAt()));
                AppProperties.setProperty("staff.updateat", (
                        staffLogged.getUpdatedAt() == null ? "" :  Format.formatDate(staffLogged.getUpdatedAt()))
                );
                return true;
            } else {
                System.out.println("Username or password wrong");
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Email or password wrong");
        }
    }


    /*REGISTER*/
    public static boolean register(Staff staff) {
//        insert DB
        String hashedPassword = BCrypt.hashpw(staff.getPassword(), BCrypt.gensalt());// string hashPassword
        staff.setPassword(hashedPassword);//set hash to password property
        staffDAO.save(staff);
        return true;
    }

    /*LOGOUT*/
    public static void logout() {
        AppProperties.setProperty("staff.loggedin", "false");
        AppProperties.setProperty("staff.staffid", "");
        AppProperties.setProperty("staff.roletitle", "");
        AppProperties.setProperty("staff.name", "");
        AppProperties.setProperty("staff.gender", "");
        AppProperties.setProperty("staff.phone", "");
        AppProperties.setProperty("staff.password", "");
        AppProperties.setProperty("staff.address", "");
        AppProperties.setProperty("staff.email", "");
        AppProperties.setProperty("staff.age", "");
        AppProperties.setProperty("staff.imagepath", "");
        AppProperties.setProperty("staff.createat", "");
        AppProperties.setProperty("staff.updateat", "");
        AppProperties.setProperty("staff.isremember", "false");
    }

    /*CHECK USER FROM FILE SAVED*/
    public static boolean authenticateFromFile(Properties properties) throws Exception {
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
            Staff staffSaved = new Staff();
            staffSaved.setPassword(properties.getProperty("staff.password"));
            staffSaved.setEmail(properties.getProperty("staff.email"));
            if(login(staffSaved)) {
                AppProperties.setProperty("staff.loggedin", "true");
                return true;
            }
        }
        return false;
    }
}
