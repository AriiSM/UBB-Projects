module com.example.socialnetwork_1v {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    exports com.example.socialnetwork_1v.starters;

    opens com.example.socialnetwork_1v to javafx.fxml;
    opens com.example.socialnetwork_1v.controller to javafx.fxml;
    exports com.example.socialnetwork_1v;

    exports com.example.socialnetwork_1v.controller;
    exports com.example.socialnetwork_1v.service;
    exports com.example.socialnetwork_1v.controller.Organizat;
    opens com.example.socialnetwork_1v.controller.Organizat to javafx.fxml;
    exports com.example.socialnetwork_1v.controller.Organizat.ADMIN;
    opens com.example.socialnetwork_1v.controller.Organizat.ADMIN to javafx.fxml;
    exports com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR;
    opens com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR to javafx.fxml;

}