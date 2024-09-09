package com.aptech.eproject2_prosmiles.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.event.ActionEvent;

public class MethodInterceptor {
    private BaseController baseController;

    public MethodInterceptor(BaseController baseController) {
        this.baseController = baseController;
    }

    public void invokeMethod(String methodName, ActionEvent event) throws NoSuchMethodException {
        try {
            Method method = baseController.getClass().getMethod(methodName, ActionEvent.class);

            // check annotation at method
            baseController.checkMethodAccess(method);

            // if access call method
            method.invoke(baseController, event);
        } catch (SecurityException e) {
            throw new NoSuchMethodException();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
