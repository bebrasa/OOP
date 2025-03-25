package nsu.kochanov.task_2_3_1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    private final SnakeGame game;
    private final GameView view;
    private Timeline timeline;

    public GameController(SnakeGame game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void startGame(Stage stage) {
        stage.setTitle("Snake");
        Scene scene = new Scene(view.getRoot());
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> handleInput(event.getCode()));

        timeline = new Timeline(new KeyFrame(Duration.millis(250), e -> {
            game.move();
            view.render(game);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleInput(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.D) game.setDirection(0);
        else if (code == KeyCode.LEFT || code == KeyCode.A) game.setDirection(1);
        else if (code == KeyCode.UP || code == KeyCode.W) game.setDirection(2);
        else if (code == KeyCode.DOWN || code == KeyCode.S) game.setDirection(3);
    }
}