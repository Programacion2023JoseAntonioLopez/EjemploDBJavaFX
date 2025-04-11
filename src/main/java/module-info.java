module com.iesochoa.ejemplodbjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.iesochoa.ejemplodbjavafx to javafx.fxml;
    exports com.iesochoa.ejemplodbjavafx;
    exports com.iesochoa.ejemplodbjavafx.controller;
    opens com.iesochoa.ejemplodbjavafx.controller to javafx.fxml;
}