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
        boolean hasAccess = false;

        for (String requiredRole : annotationRole.roles()) {
            if (currentRoleTitle.equals(requiredRole)) {
                hasAccess = true;
                System.out.println("Role " + requiredRole + " is allowed");
                break;
            }
        }

        if (!hasAccess) {
            alertAccess("Role " + currentRoleTitle + " is not allowed to access staff");
            return;
        }
    }


    /*CHECK ACCESS METHOD*/
    protected void checkMethodAccess(Method method) {
        RolePermissionRequired annotationRole = method.getAnnotation(RolePermissionRequired.class);
        String currentRoleTitle = AppProperties.getProperty("staff.roletitle");
        if (annotationRole != null) {
            boolean hasAccess = false;

            for (String requiredRole : annotationRole.roles()) {
                if (currentRoleTitle.equals(requiredRole)) {
                    hasAccess = true;
                    System.out.println("Role " + currentRoleTitle + " is allowed");
                    break;
                }
            }

            if (!hasAccess) {
                alertAccess("Role " + currentRoleTitle + " is not allowed to access this method");
            }
        }
    }



    private void alertAccess(String message) {
        DialogHelper.showNotificationDialog("Warning", message);
        throw new SecurityException(message);
    }
}
