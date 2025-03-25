package nsu.kochanov.task_2_3_1;

import javafx.scene.image.Image;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame {
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final Image[] FOODS_IMAGE = new Image[]{
            new Image(SnakeGame.class.getResource("/nsu/kochanov/task_2_3_1/img/ic_orange.png").toExternalForm()),
            new Image(SnakeGame.class.getResource("/nsu/kochanov/task_2_3_1/img/ic_apple.png").toExternalForm())
    };

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private final List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private Image foodImage;
    private int foodX, foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;

    public SnakeGame() {
        resetGame();
    }

    public void resetGame() {
        snakeBody.clear();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
        gameOver = false;
        currentDirection = RIGHT;
        score = 0;
    }

    public void move() {
        if (gameOver) return;

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).setLocation(snakeBody.get(i - 1));
        }

        switch (currentDirection) {
            case RIGHT -> snakeHead.x++;
            case LEFT -> snakeHead.x--;
            case UP -> snakeHead.y--;
            case DOWN -> snakeHead.y++;
        }

        checkCollision();
        checkFood();
    }

    private void generateFood() {
        Random random = new Random();
        while (true) {
            foodX = random.nextInt(ROWS);
            foodY = random.nextInt(COLUMNS);
            boolean foodOnSnake = snakeBody.stream().anyMatch(p -> p.x == foodX && p.y == foodY);
            if (!foodOnSnake) {
                foodImage = FOODS_IMAGE[random.nextInt(FOODS_IMAGE.length)];
                break;
            }
        }
    }

    private void checkCollision() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x >= ROWS || snakeHead.y >= COLUMNS) {
            gameOver = true;
        }
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.equals(snakeBody.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    private void checkFood() {
        if (snakeHead.x == foodX && snakeHead.y == foodY) {
            snakeBody.add(new Point(-1, -1));
            generateFood();
            score += 5;
        }
    }

    public void setDirection(int direction) {
        if ((currentDirection == LEFT && direction == RIGHT) ||
                (currentDirection == RIGHT && direction == LEFT) ||
                (currentDirection == UP && direction == DOWN) ||
                (currentDirection == DOWN && direction == UP)) {
            return;
        }
        currentDirection = direction;
    }

    public List<Point> getSnakeBody() { return snakeBody; }
    public Point getSnakeHead() { return snakeHead; }
    public int getFoodX() { return foodX; }
    public int getFoodY() { return foodY; }
    public Image getFoodImage() { return foodImage; }
    public boolean isGameOver() { return gameOver; }
    public int getScore() { return score; }
}