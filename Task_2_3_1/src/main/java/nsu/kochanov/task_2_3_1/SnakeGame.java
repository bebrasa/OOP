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
    private final List<Point> foodPositions = new ArrayList<>();
    private final List<Image> foodImages = new ArrayList<>();
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;
    private int level = 1;
    private int speed = 150; // Начальная скорость (миллисекунды задержки)

    public SnakeGame() {
        resetGame();
    }

    public void resetGame() {
        snakeBody.clear();
        snakeBody.add(new Point(5, ROWS / 2));
        gameOver = false;
        currentDirection = RIGHT;
        score = 0;
        level = 1;
        speed = 150;
        generateFood();
    }

    public void move() {
        if (gameOver) return;

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).setLocation(snakeBody.get(i - 1));
        }

        switch (currentDirection) {
            case RIGHT -> snakeBody.get(0).x++;
            case LEFT -> snakeBody.get(0).x--;
            case UP -> snakeBody.get(0).y--;
            case DOWN -> snakeBody.get(0).y++;
        }

        checkCollision();
        checkFood();
    }

    private void generateFood() {
        Random random = new Random();
        foodPositions.clear();
        foodImages.clear();

        int foodCount = random.nextInt(1, 4); // от 1 до 3 элементов еды
        for (int i = 0; i < foodCount; i++) {
            while (true) {
                int foodX = random.nextInt(ROWS);
                int foodY = random.nextInt(COLUMNS);
                boolean foodOnSnake = snakeBody.stream().anyMatch(p -> p.x == foodX && p.y == foodY);
                boolean foodExists = foodPositions.stream().anyMatch(p -> p.x == foodX && p.y == foodY);

                if (!foodOnSnake && !foodExists) {
                    foodPositions.add(new Point(foodX, foodY));
                    foodImages.add(FOODS_IMAGE[random.nextInt(FOODS_IMAGE.length)]);
                    break;
                }
            }
        }
    }

    private void checkCollision() {
        Point snakeHead = snakeBody.get(0);
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
        Point snakeHead = snakeBody.get(0);
        for (int i = 0; i < foodPositions.size(); i++) {
            if (snakeHead.equals(foodPositions.get(i))) {
                snakeBody.add(new Point(-1, -1)); // Увеличиваем змею
                foodPositions.remove(i);
                foodImages.remove(i);
                score += 5;

                // Добавляем новую еду вместо съеденной
                generateSingleFood();

                if (score % 20 == 0) { // Каждые 20 очков - новый уровень
                    level++;
                    speed = Math.max(50, speed - 20); // Уменьшаем задержку, но не меньше 50 мс
                    updateGameSpeed(); // Применяем новую скорость
                }
                break;
            }
        }
    }
    private void updateGameSpeed() {
        // Здесь должен быть код, который перезапускает таймер с новой скоростью
        System.out.println("Новый уровень: " + level + ", скорость: " + speed + " мс");
    }

    private void generateSingleFood() {
        Random random = new Random();
        while (foodPositions.size() < 3) { // Количество еды остается прежним (можно поменять на больше)
            int foodX = random.nextInt(ROWS);
            int foodY = random.nextInt(COLUMNS);
            boolean foodOnSnake = snakeBody.stream().anyMatch(p -> p.x == foodX && p.y == foodY);
            boolean foodExists = foodPositions.stream().anyMatch(p -> p.x == foodX && p.y == foodY);

            if (!foodOnSnake && !foodExists) {
                foodPositions.add(new Point(foodX, foodY));
                foodImages.add(FOODS_IMAGE[random.nextInt(FOODS_IMAGE.length)]);
            }
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
    public List<Point> getFoodPositions() { return foodPositions; }
    public List<Image> getFoodImages() { return foodImages; }
    public boolean isGameOver() { return gameOver; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getSpeed() { return speed; }
}