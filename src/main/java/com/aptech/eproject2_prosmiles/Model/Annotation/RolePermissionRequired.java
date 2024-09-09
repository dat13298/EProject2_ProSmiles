package com.aptech.eproject2_prosmiles.Model.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RolePermissionRequired {
    String[] roles() default {};
    String[] permissions() default {};
}
