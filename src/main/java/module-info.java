module com.d3dev {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    
    opens com.d3dev to javafx.fxml;
    exports com.d3dev;
}
