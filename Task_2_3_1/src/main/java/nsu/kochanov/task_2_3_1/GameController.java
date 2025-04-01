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

/**
 * Controller class for the Snake game application.
 * Manages game logic, user input, and media playback.
 */
public class GameController {
    private SnakeGame game;
    private GameView view;
    private Timeline timeline;
    private MediaPlayer mediaPlayer;

    @FXML
    private VBox root;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label levelLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private ComboBox<String> musicComboBox;

    private final Map<String, String> musicTracks = new HashMap<>();

    /**
     * Initializes the game controller.
     * Sets up game components, music selection, and game loop.
     */
    public void initialize() {
        game = new SnakeGame();
        view = new GameView(gameCanvas);
        setupMusic();
        setupGameLoop();
        updateUI();
    }

    /**
     * Configures the music tracks and combo box selection.
     */
    private void setupMusic() {
        musicTracks.put("Classic", "/nsu/kochanov/task_2_3_1/buSHIDO.mp3");
        musicTracks.put("Retro", "/nsu/kochanov/task_2_3_1/music.mp3");

        musicComboBox.getItems().addAll(musicTracks.keySet());
        musicComboBox.getSelectionModel().selectFirst();
        musicComboBox.setOnAction(event -> changeMusic());

        changeMusic();
    }

    /**
     * Changes the currently playing music track.
     */
    private void changeMusic() {
        String selected = musicComboBox.getSelectionModel().getSelectedItem();
        String musicPath = musicTracks.get(selected);

        try {
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

            if (timeline != null
                    && timeline.getStatus() == Timeline.Status.RUNNING) {
                mediaPlayer.play();
            }
        }
        catch (Exception e) {
            System.err.println("Error loading music: " + e.getMessage());
        }
    }

    /**
     * Sets up the main game loop timeline.
     */
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

    /**
     * Handles the game start action.
     */
    @FXML
    private void handleStart() {
        timeline.play();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Handles the game stop action.
     */
    @FXML
    private void handleStop() {
        timeline.stop();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Handles the game restart action.
     */
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

    /**
     * Updates the game speed based on current level.
     */
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

    /**
     * Sets up the game scene and key listeners.
     *
     * @param stage The primary stage for the game
     */
    public void setScene(Stage stage) {
        stage.setTitle("Snake Game");
        stage.getScene().setOnKeyPressed(
                event -> handleInput(event.getCode())
        );
    }

    /**
     * Handles keyboard input for controlling the snake.
     *
     * @param code The key code of the pressed key
     */
    private void handleInput(KeyCode code) {
        switch (code) {
            case RIGHT:
            case D:
                game.setDirection(SnakeGame.RIGHT);
                break;
            case LEFT:
            case A:
                game.setDirection(SnakeGame.LEFT);
                break;
            case UP:
            case W:
                game.setDirection(SnakeGame.UP);
                break;
            case DOWN:
            case S:
                game.setDirection(SnakeGame.DOWN);
                break;
            default:
                // Ignore other keys
                break;
        }
    }

    /**
     * Updates the game UI with current level and score.
     */
    private void updateUI() {
        levelLabel.setText(String.format("Level: %d", game.getLevel()));
        scoreLabel.setText(String.format("Score: %d", game.getScore()));
    }
}