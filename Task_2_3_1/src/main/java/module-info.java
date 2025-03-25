module nsu.kochanov.task_2_3_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens nsu.kochanov.task_2_3_1 to javafx.fxml;
    exports nsu.kochanov.task_2_3_1;
}