package com.aptech.eproject2_prosmiles.Controller;

import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.Model.Annotation.RolePermissionRequired;
import javafx.fxml.Initializable;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        RolePermissionRequired annotation = getClass().getAnnotation(RolePermissionRequired.class);
        if (annotation != null) {
            checkAccess(annotation);
        }
    }

    /*CHECK ACCESS CLASS*/
    private void checkAccess(RolePermissionRequired annotationRole) {
        String currentRoleTitle = AppProperties.getProperty("staff.roletitle");
        //check role required
        for(String requiredRole : annotationRole.roles()){
            if(!currentRoleTitle.equals(requiredRole)){
                alertAccess("Role " + currentRoleTitle + " is not allowed to access staff");
                return;
            } else System.out.println("role " + requiredRole + " is allowed");
        }
        //check permission required
//        for (String requiredPermission : annotationRole.permissions()){
//            if(!currentStaff.hasPermission(requiredPermission)){
//                alertAccess("Permission " + requiredPermission + " is not allowed to access staff");
//                return;
//            } else System.out.println("permission " + requiredPermission + " is allowed");
//        }
    }

    /*CHECK ACCESS METHOD*/
    protected void checkMethodAccess(Method method) {
        RolePermissionRequired annotationRole = method.getAnnotation(RolePermissionRequired.class);

        String currentRoleTitle = AppProperties.getProperty("staff.roletitle");

        if(annotationRole != null){
            for(String requiredRole : annotationRole.roles()){
                //check role required
                if(!currentRoleTitle.equals(requiredRole)){
                    alertAccess("Role " + currentRoleTitle + " is not allowed to access staff");
                    return;
                } else System.out.println("role " + requiredRole + " is allowed");
            }
//            for (String requiredPermission : annotationRole.permissions()){
//                if(!currentStaff.hasPermission(requiredPermission)){
//                    alertAccess("Permission " + requiredPermission + " is not allowed to access staff");
//                } else System.out.println("permission " + requiredPermission + " is allowed");
//            }
        }
    }


    private void alertAccess(String message) {
        // Alert here (Use actual alert mechanism instead of print)
        DialogHelper.showNotificationDialog("Warning", message);
//        System.out.println("Access denied: " + message);
        throw new SecurityException(message);
        // You can also add code to halt the application or redirect to an error page
    }
}
