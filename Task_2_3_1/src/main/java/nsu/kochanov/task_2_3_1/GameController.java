package nsu.kochanov.task_2_3_1;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    private SnakeGame game;
    private GameView view;
    private Timeline timeline;
    private MediaPlayer mediaPlayer;

    @FXML private VBox root;
    @FXML private Canvas gameCanvas;
    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private ComboBox<String> musicComboBox;

    private final Map<String, String> musicTracks = new HashMap<>();

    public void initialize() {
        game = new SnakeGame();
        view = new GameView(gameCanvas);
        setupMusic();
        setupGameLoop();
        updateUI();
    }

    private void setupMusic() {
        // Загрузка музыки из ресурсов (не из файловой системы!)
        musicTracks.put("Classic", "/nsu/kochanov/task_2_3_1/buSHIDO.mp3");
        musicTracks.put("Retro", "/nsu/kochanov/task_2_3_1/music.mp3");

        musicComboBox.getItems().addAll(musicTracks.keySet());
        musicComboBox.getSelectionModel().selectFirst();
        musicComboBox.setOnAction(event -> changeMusic());

        // Инициализация первой музыки
        changeMusic();
    }

    private void changeMusic() {
        String selected = musicComboBox.getSelectionModel().getSelectedItem();
        String musicPath = musicTracks.get(selected);

        try {
            // Правильный способ загрузки из ресурсов
            URL resourceUrl = getClass().getResource(musicPath);
            if (resourceUrl == null) {
                System.err.println("Music file not found: " + musicPath);
                return;
            }

            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            Media media = new Media(resourceUrl.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Автовоспроизведение если игра активна
            if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
        }
    }

    private void setupGameLoop() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(game.getSpeed()),
                e -> {
                    game.move();
                    view.render(game);
                    updateUI();
                    updateSpeed();
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void handleStart() {
        timeline.play();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void handleStop() {
        timeline.stop();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void handleRestart() {
        game.resetGame();
        view.render(game);
        updateUI();
        updateSpeed();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.play();
        }
        timeline.play();
    }

    // Остальные методы без изменений...
    private void updateSpeed() {
        timeline.stop();
        timeline.getKeyFrames().setAll(new KeyFrame(
                Duration.millis(game.getSpeed()),
                e -> {
                    game.move();
                    view.render(game);
                    updateUI();
                }
        ));
        timeline.play();
    }

    public void setScene(Stage stage) {
        stage.setTitle("Snake Game");
        stage.getScene().setOnKeyPressed(
                event -> handleInput(event.getCode())
        );
    }

    private void handleInput(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.D) {
            game.setDirection(SnakeGame.RIGHT);
        } else if (code == KeyCode.LEFT || code == KeyCode.A) {
            game.setDirection(SnakeGame.LEFT);
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            game.setDirection(SnakeGame.UP);
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            game.setDirection(SnakeGame.DOWN);
        }
    }

    private void updateUI() {
        levelLabel.setText(String.format("Level: %d", game.getLevel()));
        scoreLabel.setText(String.format("Score: %d", game.getScore()));
    }
}