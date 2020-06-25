package sample;

import sample.common.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sample.common.Config.*;

interface GameOverListener {
    void onOver();
}

interface NewPointListener {
    void onPointCollected(long totalPoints);
}

interface DamageTakenListener {
    void onDamageTaken(int newHealth);
}

public class Game {
    private final Random rand;

    private class FallingObject extends Object {
        public double speed;

        public FallingObject(double size, double speed) {
            super(rand.nextDouble() * (1.0 - size) + size / 2.0, -size / 2.0, size, size);
            this.speed = speed;
        }

        public void update(double delta, double acceleration) {
            this.y += Math.min(this.height, this.speed * acceleration * delta);
        }
    }

    private boolean running = false;
    private int health = 3;
    private double timer = 0;
    private long points = 0;
    private final Object paddle = new Object(0.5, 1.0 - PLATFORM_HEIGHT / 2.0, PLATFORM_WIDTH, PLATFORM_HEIGHT);
    private final List<FallingObject> bubbles = new ArrayList<>();
    private final List<FallingObject> meals = new ArrayList<>();

    private final GameOverListener gameOverListener;
    private final NewPointListener pointListener;
    private final DamageTakenListener damageTakenListener;

    public Game(GameOverListener gameOverListener, NewPointListener pointListener, DamageTakenListener damageTakenListener) {
        this.gameOverListener = gameOverListener;
        this.pointListener = pointListener;
        this.damageTakenListener = damageTakenListener;
        rand = new Random();
    }

    public Object getPaddle() {
        return paddle;
    }

    public List<FallingObject> getBubbles() {
        return bubbles;
    }

    public List<FallingObject> getMeals() {
        return meals;
    }

    public boolean isRunning() {
        return running;
    }

    public void startGame() {
        this.running = true;
        this.health = 3;
        this.timer = 0;
        this.points = 0;
        this.bubbles.clear();
        this.meals.clear();
        this.paddle.x = 0.5;
    }

    private FallingObject createFallingObject() {
        double randomSize = rand.nextDouble() * (MAX_BUBBLE_SIZE - MIN_BUBBLE_SIZE) + MIN_BUBBLE_SIZE;
        double randomSpeed = rand.nextDouble() * (MAX_BUBBLE_SPEED - MIN_BUBBLE_SPEED) + MIN_BUBBLE_SPEED;
        return new FallingObject(randomSize, randomSpeed);
    }

    private void trySpawnBubble() {
        if (rand.nextDouble() > 1.0 / 60.0) {
            return;
        }

        bubbles.add(createFallingObject());
    }

    private void trySpawnMeal() {
        if (rand.nextDouble() > 0.1 / 60.0) {
            return;
        }

        meals.add(createFallingObject());
    }

    private boolean isPaddleColliding(FallingObject obj) {
        double paddleUp = paddle.y - paddle.height / 2.0;
        double paddleLeft = paddle.x - paddle.width / 2.0;
        double paddleRight = paddle.x + paddle.width / 2.0;
        if (obj.y + obj.height / 2.0 > paddleUp && obj.x > paddleLeft && obj.x < paddleRight) {
            return true;
        }
        return Math.sqrt(Math.pow(obj.x - paddleLeft, 2) + Math.pow(obj.y - paddleUp, 2)) < obj.width / 2.0 ||
                Math.sqrt(Math.pow(obj.x - paddleRight, 2) + Math.pow(obj.y - paddleUp, 2)) < obj.width / 2.0;
    }

    public void update(double delta, boolean left, boolean right) {
        this.timer += delta;

        int dir = left ? -1 : right ? 1 : 0;
        paddle.x = Math.min(1.0 - paddle.width / 2, Math.max(paddle.width / 2, paddle.x + PLATFORM_SPEED * delta * dir));

        List<FallingObject> diedBubbles = new ArrayList<>();
        bubbles.forEach(bubble -> {
            bubble.update(delta, 1.0 + timer * ACCELERATION_FACTOR);
            if (isPaddleColliding(bubble)) {
                this.health -= BUBBLE_DAMAGE;
                this.damageTakenListener.onDamageTaken(this.health);
                if (this.health <= 0) {
                    this.running = false;
                    this.gameOverListener.onOver();
                }

                diedBubbles.add(bubble);
            }

            if (bubble.y - bubble.height / 2.0 > 1) {
                diedBubbles.add(bubble);
            }
        });
        bubbles.removeAll(diedBubbles);

        List<FallingObject> diedMeals = new ArrayList<>();
        meals.forEach(meal -> {
            meal.update(delta, 1.0);
            if (isPaddleColliding(meal)) {
                this.pointListener.onPointCollected(++this.points);
                diedMeals.add(meal);
            }
            if (meal.y - meal.height / 2.0 > 1) {
                diedMeals.add(meal);
            }
        });
        meals.removeAll(diedMeals);

        trySpawnBubble();
        trySpawnMeal();
    }
}
