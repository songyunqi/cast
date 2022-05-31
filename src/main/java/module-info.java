module com.foo.cast {
    requires javafx.controls;
    requires javafx.fxml;
    requires epublib.core;
    requires org.jsoup;
    requires org.apache.poi.poi;
    requires org.apache.commons.collections4;
    requires tencentcloud.speech.sdk.java;
    requires com.google.common;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires java.sql;

    opens com.foo.cast to javafx.fxml;
    exports com.foo.cast;
}