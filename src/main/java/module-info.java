module com.aptech.eproject2_prosmiles {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires mysql.connector.java;
    requires jbcrypt;
    requires jdk.jshell;
    requires java.desktop;
    requires kernel;
    requires layout;

    opens com.aptech.eproject2_prosmiles to javafx.fxml;
    exports com.aptech.eproject2_prosmiles;
    exports com.aptech.eproject2_prosmiles.Controller;
    opens com.aptech.eproject2_prosmiles.Controller to javafx.fxml;

    opens com.aptech.eproject2_prosmiles.Model.Entity to javafx.base;
}