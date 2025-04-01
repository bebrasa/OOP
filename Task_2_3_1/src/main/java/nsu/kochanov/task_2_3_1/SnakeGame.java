package nsu.kochanov.task_2_3_1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Main game logic class for Snake game.
 * Handles game state, movement, collisions and food generation.
 */
public class SnakeGame {
    public static final int ROWS = 20;
    public static final int COLUMNS = ROWS;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    private static final Image[] FOODS_IMAGE = new Image[] {
            new Image(Objects.requireNonNull(
                    SnakeGame.class.getResource(
                            "/nsu/kochanov/task_2_3_1/img/ic_orange.png"
                    )
            ).toExternalForm()),
            new Image(Objects.requireNonNull(
                    SnakeGame.class.getResource(
                            "/nsu/kochanov/task_2_3_1/img/ic_apple.png"
                    )
            ).toExternalForm())
    };

    private final List<Point> snakeBody = new ArrayList<>();
    private final List<Point> foodPositions = new ArrayList<>();
    private final List<Image> foodImages = new ArrayList<>();
    private Bot bot1;
    private Bot bot2;
    private final Random random = new Random();

    private boolean gameOver;
    private int currentDirection;
    private int score = 0;
    private int level = 1;
    private int speed = 150;

    /**
     * Initializes new game with bots and resets game state.
     */
    public SnakeGame() {
        initializeBots();
        resetGame();
    }

    /**
     * Creates new bot instances with initial positions and colors.
     */
    private void initializeBots() {
        bot1 = new Bot(this, ROWS - 5, 5, Color.web("FF5733"));
        bot2 = new Bot(this, 5, ROWS - 5, Color.web("33FF57"));
    }

    /**
     * Resets game to initial state.
     * Clears all entities and sets default values.
     */
    public void resetGame() {
        snakeBody.clear();
        foodPositions.clear();
        foodImages.clear();
        snakeBody.add(new Point(5, ROWS / 2));

        gameOver = false;
        currentDirection = RIGHT;
        score = 0;
        level = 1;
        speed = 150;

        initializeBots();
        generateFood();
    }

    /**
     * Moves all game entities (player snake and bots).
     * Checks for collisions and food after movement.
     */
    public void move() {
        if (gameOver) {
            return;
        }

        moveSnake();
        bot1.move();
        bot2.move();
        checkCollision();
        checkFood();
    }

    /**
     * Moves player snake based on current direction.
     */
    private void moveSnake() {
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).setLocation(snakeBody.get(i - 1));
        }

        Point head = snakeBody.getFirst();
        switch (currentDirection) {
            case RIGHT:
                head.x++;
                break;
            case LEFT:
                head.x--;
                break;
            case UP:
                head.y--;
                break;
            case DOWN:
                head.y++;
                break;
            default:
                break;
        }
    }

    /**
     * Generates new food items until reaching minimum count.
     */
    private void generateFood() {
        int neededFood = 3 - foodPositions.size();
        int attempts = 0;
        int maxAttempts = 100;

        while (neededFood > 0 && attempts < maxAttempts) {
            attempts++;
            Point newFood = new Point(
                    random.nextInt(ROWS),
                    random.nextInt(COLUMNS));

            if (!isPositionOccupied(newFood)) {
                foodPositions.add(newFood);
                foodImages.add(FOODS_IMAGE[
                        random.nextInt(FOODS_IMAGE.length)]);
                neededFood--;
            }
        }
    }

    /**
     * Checks if position is occupied by any game entity.
     *
     * @param point Position to check
     * @return True if position is occupied
     */
    private boolean isPositionOccupied(Point point) {
        return snakeBody.contains(point)
                || bot1.getBody().contains(point)
                || bot2.getBody().contains(point)
                || foodPositions.contains(point);
    }

    /**
     * Checks for collisions with walls, self and bots.
     */
    private void checkCollision() {
        Point head = snakeBody.getFirst();

        if (head.x < 0
                || head.y < 0
                || head.x >= ROWS
                || head.y >= COLUMNS) {
            gameOver = true;
            return;
        }

        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.equals(snakeBody.get(i))) {
                gameOver = true;
                return;
            }
        }

        if (isPointInBotBody(head, bot1)
                || isPointInBotBody(head, bot2)) {
            gameOver = true;
        }
        checkBotCollisions();
    }

    /**
     * Checks and resolves collisions between bots.
     */
    private void checkBotCollisions() {
        Point bot1Head = bot1.getBody().getFirst();
        Point bot2Head = bot2.getBody().getFirst();

        if (bot1Head.equals(bot2Head)) {
            bot1.changeDirection((bot1.getDirection() + 1) % 4);
            bot2.changeDirection((bot2.getDirection() + 3) % 4);
            bot1.move();
            bot2.move();
        }
        checkBotSideCollision();
    }

    /**
     * Checks and resolves side-by-side bot collisions.
     */
    private void checkBotSideCollision() {
        Point bot1Head = bot1.getBody().getFirst();
        Point bot2Head = bot2.getBody().getFirst();

        if (Math.abs(bot1Head.x - bot2Head.x)
                + Math.abs(bot1Head.y - bot2Head.y) == 1) {
            if (random.nextBoolean()) {
                bot1.changeDirection((bot1.getDirection() + 1) % 4);
            } else {
                bot2.changeDirection((bot2.getDirection() + 2) % 4);
            }
        }
    }

    /**
     * Checks if point is part of bot's body.
     *
     * @param point Point to check
     * @param bot Bot to check against
     * @return True if point is in bot's body
     */
    private boolean isPointInBotBody(Point point, Bot bot) {
        return bot.getBody().stream().anyMatch(p -> p.equals(point));
    }

    /**
     * Checks if any entity ate food and handles consequences.
     */
    private void checkFood() {
        Point playerHead = snakeBody.getFirst();
        Point bot1Head = bot1.getBody().getFirst();
        Point bot2Head = bot2.getBody().getFirst();
        List<Integer> foodToRemove = new ArrayList<>();

        for (int i = 0; i < foodPositions.size(); i++) {
            Point food = foodPositions.get(i);
            boolean eaten = false;

            if (playerHead.equals(food)) {
                snakeBody.add(new Point(-1, -1));
                score += 5;
                eaten = true;
                if (score % 20 == 0) {
                    level++;
                    speed = Math.max(50, speed - 20);
                }
            } else if (bot1Head.equals(food)) {
                bot1.getBody().add(new Point(-1, -1));
                eaten = true;
            } else if (bot2Head.equals(food)) {
                bot2.getBody().add(new Point(-1, -1));
                eaten = true;
            }
            if (eaten) {
                foodToRemove.add(i);
            }
        }

        for (int i = foodToRemove.size() - 1; i >= 0; i--) {
            int index = foodToRemove.get(i);
            foodPositions.remove(index);
            foodImages.remove(index);
        }

        if (foodPositions.size() < 3) {
            generateFood();
        }
    }

    /**
     * Changes player snake direction if valid.
     *
     * @param direction New direction (RIGHT, LEFT, UP, DOWN)
     */
    public void setDirection(int direction) {
        if ((currentDirection == LEFT && direction == RIGHT)
                || (currentDirection == RIGHT && direction == LEFT)
                || (currentDirection == UP && direction == DOWN)
                || (currentDirection == DOWN && direction == UP)) {
            return;
        }
        currentDirection = direction;
    }

    /**
     * Gets all bodies in the game.
     *
     * @return List containing all snake bodies
     */
    public List<List<Point>> getAllBodies() {
        return List.of(snakeBody, bot1.getBody(), bot2.getBody());
    }

    /**
     * Gets player snake body.
     *
     * @return List of points representing snake body
     */
    public List<Point> getSnakeBody() {
        return snakeBody;
    }

    /**
     * Gets current food positions.
     *
     * @return List of food positions
     */
    public List<Point> getFoodPositions() {
        return foodPositions;
    }

    /**
     * Gets food images.
     *
     * @return List of food images
     */
    public List<Image> getFoodImages() {
        return foodImages;
    }

    /**
     * Checks if game is over.
     *
     * @return True if game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets current score.
     *
     * @return Current game score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets current level.
     *
     * @return Current game level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets current game speed.
     *
     * @return Current game speed in milliseconds
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets first bot instance.
     *
     * @return First bot
     */
    public Bot getBot1() {
        return bot1;
    }

    /**
     * Gets second bot instance.
     *
     * @return Second bot
     */
    public Bot getBot2() {
        return bot2;
    }
}