module com.iesochoa.ejemplodbjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.iesochoa.ejemplodbjavafx to javafx.fxml;
    exports com.iesochoa.ejemplodbjavafx;
}