package nsu.kochanov.task_2_3_1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * Represents an AI-controlled bot in the Snake game.
 * Handles bot movement, food targeting and collision avoidance.
 */
public class Bot {
    private final List<Point> body;
    private int direction;
    private final SnakeGame game;
    private final Color color;
    private final Random random = new Random();
    private Point targetFood;
    private int stuckCounter = 0;

    /**
     * Creates a new bot instance.
     * @param game Reference to the main game instance
     * @param startX Initial x-coordinate
     * @param startY Initial y-coordinate
     * @param color Bot color
     */
    public Bot(SnakeGame game, int startX, int startY, Color color) {
        this.game = game;
        this.body = new ArrayList<>();
        this.body.add(new Point(startX, startY));
        this.color = color;
        this.direction = random.nextInt(4);
    }

    /**
     * Updates bot position and state.
     */
    public void move() {
        if (body.isEmpty()) {
            return;
        }

        Point head = body.getFirst();
        updateTargetFood(head);

        if (targetFood != null) {
            int newDirection = calculateBestDirection(head, targetFood);
            if (newDirection != -1) {
                direction = newDirection;
                stuckCounter = 0;
            } else {
                stuckCounter++;
                if (stuckCounter > 5) {
                    direction = findRandomSafeDirection(head);
                }
            }
        } else {
            direction = findRandomSafeDirection(head);
        }

        moveInDirection();
        checkFood();
    }

    /**
     * Checks if bot has reached food.
     */
    private void checkFood() {
        Point head = body.getFirst();
        List<Point> foodPositions = game.getFoodPositions();

        // Создаем копию списка для безопасного удаления
        List<Point> foodsToRemove = new ArrayList<>();

        for (Point food : foodPositions) {
            if (head.equals(food)) {
                body.add(new Point(-1, -1));
                foodsToRemove.add(food);
                break; // Бот может съесть только одну еду за ход
            }
        }

        // Удаляем съеденную еду через основной игровой объект
        game.getFoodPositions().removeAll(foodsToRemove);
        if (!foodsToRemove.isEmpty()) {
            game.getFoodImages().removeFirst(); // Удаляем соответствующее изображение
        }
    }

    // Остальные методы остаются без изменений...
    private void updateTargetFood(Point head) {
        targetFood = findNearestFood(head);
        if (targetFood != null && !game.getFoodPositions().contains(targetFood)) {
            targetFood = findNearestFood(head);
        }
    }

    private Point findNearestFood(Point head) {
        return game.getFoodPositions().stream()
                .min(Comparator.comparingInt(food ->
                        Math.abs(food.x - head.x) + Math.abs(food.y - head.y)))
                .orElse(null);
    }

    private int calculateBestDirection(Point head, Point target) {
        List<Integer> possibleDirections = new ArrayList<>();
        int dx = target.x - head.x;
        int dy = target.y - head.y;

        if (dx > 0 && isDirectionSafe(SnakeGame.RIGHT, head)) {
            possibleDirections.add(SnakeGame.RIGHT);
        }
        if (dx < 0 && isDirectionSafe(SnakeGame.LEFT, head)) {
            possibleDirections.add(SnakeGame.LEFT);
        }
        if (dy > 0 && isDirectionSafe(SnakeGame.DOWN, head)) {
            possibleDirections.add(SnakeGame.DOWN);
        }
        if (dy < 0 && isDirectionSafe(SnakeGame.UP, head)) {
            possibleDirections.add(SnakeGame.UP);
        }

        return possibleDirections.isEmpty()
                ? -1
                : possibleDirections.get(random.nextInt(possibleDirections.size()));
    }

    private boolean isDirectionSafe(int direction, Point head) {
        Point next = new Point(head);
        switch (direction) {
            case SnakeGame.RIGHT -> next.x++;
            case SnakeGame.LEFT -> next.x--;
            case SnakeGame.UP -> next.y--;
            case SnakeGame.DOWN -> next.y++;
        }
        return isPositionSafe(next);
    }

    private boolean isPositionSafe(Point point) {
        if (point.x < 0 || point.y < 0 ||
                point.x >= SnakeGame.ROWS || point.y >= SnakeGame.COLUMNS) {
            return false;
        }
        return game.getAllBodies().stream()
                .noneMatch(body -> body.stream().anyMatch(p -> p.equals(point)));
    }

    private int findRandomSafeDirection(Point head) {
        List<Integer> safeDirections = new ArrayList<>();
        for (int dir = 0; dir < 4; dir++) {
            if (isDirectionSafe(dir, head)) {
                safeDirections.add(dir);
            }
        }
        return safeDirections.isEmpty()
                ? direction
                : safeDirections.get(random.nextInt(safeDirections.size()));
    }

    protected void moveInDirection() {
        for (int i = body.size() - 1; i >= 1; i--) {
            body.get(i).setLocation(body.get(i - 1));
        }

        Point head = body.getFirst();
        switch (direction) {
            case SnakeGame.RIGHT -> head.x++;
            case SnakeGame.LEFT -> head.x--;
            case SnakeGame.UP -> head.y--;
            case SnakeGame.DOWN -> head.y++;
        }
    }

    public void changeDirection(int newDirection) {
        if (newDirection >= 0 && newDirection <= 3) {
            this.direction = newDirection;
        }
    }

    public int getDirection() {
        return direction;
    }

    public List<Point> getBody() {
        return body;
    }

    public Color getColor() {
        return color;
    }
}