module nsu.kochanov.task231 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens nsu.kochanov.task231 to javafx.fxml;
    exports nsu.kochanov.task231;
}