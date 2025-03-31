package nsu.kochanov.task_2_3_1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class GameController {
    private SnakeGame game;
    private GameView view;
    private Timeline timeline;
    private MediaPlayer mediaPlayer;

    @FXML private VBox root;
    @FXML private Canvas gameCanvas;
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;

    public void initialize() {
        game = new SnakeGame();
        view = new GameView(gameCanvas);
        setupGameLoop();
        updateUI();
        setupMusic();// Устанавливаем начальные значения UI
    }

    private void setupMusic() {
        String musicFile = "src/main/resources/nsu/kochanov/task_2_3_1/music.mp3"; // Убедись, что путь правильный
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Зацикливаем
    }

    private void setupGameLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(game.getSpeed()), e -> {
            game.move();
            view.render(game);
            updateUI();
            updateSpeed();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateSpeed() {
        timeline.stop();
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(game.getSpeed()), e -> {
            game.move();
            view.render(game);
            updateUI();
        }));
        timeline.play();
    }

    @FXML
    private void handleStart() {
        timeline.play();
        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void handleStop() {
        timeline.stop();
        mediaPlayer.pause();
    }

    @FXML
    private void handleRestart() {
        game.resetGame();
        view.render(game);
        updateUI();
        updateSpeed();
        timeline.play();
        mediaPlayer.stop();
        mediaPlayer.play(); // Перезапускаем музыку
    }

    public void setScene(Stage stage) {
        stage.setTitle("Snake");
        stage.getScene().setOnKeyPressed(event -> handleInput(event.getCode()));
    }

    private void handleInput(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.D) game.setDirection(0);
        else if (code == KeyCode.LEFT || code == KeyCode.A) game.setDirection(1);
        else if (code == KeyCode.UP || code == KeyCode.W) game.setDirection(2);
        else if (code == KeyCode.DOWN || code == KeyCode.S) game.setDirection(3);
    }

    private void updateUI() {
        levelLabel.setText("Level: " + game.getLevel());
        scoreLabel.setText("Score: " + game.getScore());
    }
}