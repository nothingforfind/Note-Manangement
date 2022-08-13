module com.example.notemanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.notemanagement to javafx.fxml;
    exports com.example.notemanagement;
}