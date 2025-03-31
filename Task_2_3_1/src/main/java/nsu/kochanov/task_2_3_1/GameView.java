package nsu.kochanov.task_2_3_1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.Point;
import java.util.List;

public class GameView {
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int SQUARE_SIZE = WIDTH / 20;

    private final Canvas canvas;
    private final GraphicsContext gc;

    public GameView(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void render(SnakeGame game) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawBackground();
        drawFood(game.getFoodPositions(), game.getFoodImages());
        drawSnake(game.getSnakeBody());
        drawScore(game.getScore());

        if (game.isGameOver()) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
        }
    }

    private void drawBackground() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                gc.setFill((i + j) % 2 == 0 ? Color.web("AAD751") : Color.web("A2D149"));
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawFood(List<Point> foodPositions, List<Image> foodImages) {
        for (int i = 0; i < foodPositions.size(); i++) {
            Point foodPos = foodPositions.get(i);
            Image foodImage = foodImages.get(i);
            gc.drawImage(foodImage, foodPos.x * SQUARE_SIZE, foodPos.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    private void drawSnake(List<Point> snake) {
        gc.setFill(Color.web("4674E9"));
        for (Point p : snake) {
            gc.fillRoundRect(p.x * SQUARE_SIZE, p.y * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void drawScore(int score) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }
}