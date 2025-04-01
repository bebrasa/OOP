package nsu.kochanov.task_2_3_1;

import java.awt.Point;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Handles rendering of all game elements on the canvas.
 * Responsible for drawing snakes, food, background and game over screen.
 */
public class GameView {
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int SQUARE_SIZE = WIDTH / 20;
    private static final Font GAME_OVER_FONT = new Font("Digital-7", 70);
    private static final Font SCORE_FONT = new Font("Digital-7", 35);

    private final GraphicsContext gc;

    /**
     * Creates new GameView with specified canvas.
     * @param canvas Canvas to draw game elements on
     */
    public GameView(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * Renders complete game state including:
     * - Background grid
     * - Food items
     * - Player snake and bots
     * - Score display
     * - Game over screen when needed
     * @param game Current game state to render
     */
    public void render(SnakeGame game) {
        clearCanvas();
        drawBackground();
        drawFood(game.getFoodPositions(), game.getFoodImages());
        drawSnake(game.getSnakeBody(), Color.web("4674E9"));
        drawScore(game.getScore());
        drawSnake(game.getBot1().getBody(), game.getBot1().getColor());
        drawSnake(game.getBot2().getBody(), game.getBot2().getColor());

        if (game.isGameOver()) {
            drawGameOver();
        }
    }

    /**
     * Clears the entire canvas.
     */
    private void clearCanvas() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Draws checkerboard background pattern.
     */
    private void drawBackground() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Color color = (i + j) % 2 == 0
                        ? Color.web("AAD751")
                        : Color.web("A2D149");
                gc.setFill(color);
                gc.fillRect(
                        i * SQUARE_SIZE,
                        j * SQUARE_SIZE,
                        SQUARE_SIZE,
                        SQUARE_SIZE);
            }
        }
    }

    /**
     * Draws all food items on the canvas.
     * @param foodPositions List of food positions
     * @param foodImages List of food images corresponding to positions
     */
    private void drawFood(List<Point> foodPositions, List<Image> foodImages) {
        for (int i = 0; i < foodPositions.size(); i++) {
            Point foodPos = foodPositions.get(i);
            Image foodImage = foodImages.get(i);
            gc.drawImage(
                    foodImage,
                    foodPos.x * SQUARE_SIZE,
                    foodPos.y * SQUARE_SIZE,
                    SQUARE_SIZE,
                    SQUARE_SIZE);
        }
    }

    /**
     * Draws a snake (player or bot) with specified color.
     * @param snake List of points representing snake body
     * @param color Color to draw the snake
     */
    private void drawSnake(List<Point> snake, Color color) {
        gc.setFill(color);
        for (Point p : snake) {
            gc.fillRoundRect(
                    p.x * SQUARE_SIZE,
                    p.y * SQUARE_SIZE,
                    SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1,
                    20,
                    20);
        }
    }

    /**
     * Draws current score in top-left corner.
     * @param score Current game score
     */
    private void drawScore(int score) {
        gc.setFill(Color.WHITE);
        gc.setFont(SCORE_FONT);
        gc.fillText("Score: " + score, 10, 35);
    }

    /**
     * Draws "Game Over" message centered on screen.
     */
    private void drawGameOver() {
        gc.setFill(Color.RED);
        gc.setFont(GAME_OVER_FONT);
        gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
    }
}