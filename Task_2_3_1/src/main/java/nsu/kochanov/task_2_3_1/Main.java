package nsu.kochanov.task_2_3_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * javadoc.
 */
public class Main extends Application {
    /**
     * javadoc.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/nsu/kochanov/task_2_3_1/sample.fxml"));
        Parent root = loader.load();

        GameController controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.setScene(primaryStage);
    }

    /**
     * javadoc.
     */
    public static void main(String[] args) {
        launch(args);
    }
}