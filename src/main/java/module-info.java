module com.example.compmath2javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.compmath2javafx to javafx.fxml;
    exports com.example.compmath2javafx;
}