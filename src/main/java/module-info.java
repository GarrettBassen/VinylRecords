module com.ags.vr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mybatis;


    opens com.ags.vr to javafx.fxml;
    exports com.ags.vr;
    exports com.ags.vr.controllers;
    opens com.ags.vr.controllers to javafx.fxml;
}