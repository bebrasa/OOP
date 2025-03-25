package nsu.kochanov.task_2_3_1;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SnakeGame game = new SnakeGame();
        GameView view = new GameView();
        GameController controller = new GameController(game, view);

        controller.startGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}