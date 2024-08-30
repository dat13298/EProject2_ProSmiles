module com.aptech.eproject2_prosmiles {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires jbcrypt;
    requires jdk.jshell;

    opens com.aptech.eproject2_prosmiles to javafx.fxml;
    exports com.aptech.eproject2_prosmiles;
    exports com.aptech.eproject2_prosmiles.Controller;
    opens com.aptech.eproject2_prosmiles.Controller to javafx.fxml;
}