module com.d3dev {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires java.desktop;
    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.javacpp;

    opens com.d3dev to javafx.fxml;
    exports com.d3dev;
}
