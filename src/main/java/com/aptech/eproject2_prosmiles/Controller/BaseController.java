package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Repository.StaffDAO;
import javafx.fxml.Initializable;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    private StaffDAO staffDAO = new StaffDAO();
    protected Staff currentStaff;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /*LOAD CURRENT STAFF*/
    public void loadCurrentStaff() {
        int staffPropertiesId = Integer.parseInt(AppProperties.getProperty("staff.staffid"));
        currentStaff = staffDAO.getById(staffPropertiesId);
        String roleProperties = AppProperties.getProperty("staff.roletitle");
        currentStaff.getRole().setTitle(roleProperties);
    }

    /*CHECK ACCESS CLASS*/
    private void checkAccess(RolePermissionRequired annotationRole) {
        //check role required
        for(String requiredRole : annotationRole.roles()){
            if(!currentStaff.hasRole(requiredRole)){
                alertAccess("Role " + requiredRole + " is not allowed to access staff");
                return;
            } else System.out.println("role " + requiredRole + " is allowed");
        }
        //check permission required
        for (String requiredPermission : annotationRole.permissions()){
            if(!currentStaff.hasPermission(requiredPermission)){
                alertAccess("Permission " + requiredPermission + " is not allowed to access staff");
                return;
            } else System.out.println("permission " + requiredPermission + " is allowed");
        }
    }

    /*CHECK ACCESS METHOD*/
    private void checkAccessMethod(Method method) {
        RolePermissionRequired annotationRole = method.getAnnotation(RolePermissionRequired.class);

        if(annotationRole != null){
            for(String requiredRole : annotationRole.roles()){
                //check role required
                if(!currentStaff.hasRole(requiredRole)){
                    alertAccess("Role " + requiredRole + " is not allowed to access staff");
                    return;
                } else System.out.println("role " + requiredRole + " is allowed");
            }
            for (String requiredPermission : annotationRole.permissions()){
                if(!currentStaff.hasPermission(requiredPermission)){
                    alertAccess("Permission " + requiredPermission + " is not allowed to access staff");
                } else System.out.println("permission " + requiredPermission + " is allowed");
            }
        }
    }


    private void alertAccess(String message) {
        // Alert here (Use actual alert mechanism instead of print)
        System.out.println("Access denied: " + message);
        throw new SecurityException(message);
        // You can also add code to halt the application or redirect to an error page
    }
}
