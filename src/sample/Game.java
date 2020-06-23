package sample;

import sample.common.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sample.common.Config.*;

public class Game {
    private Random rand;

    private class Bubble extends Object {
        public double speed;
        public Bubble(double size, double speed) {
            super(rand.nextDouble()*(1.0 - size) + size/2.0, -size/2.0, size, size);
            this.speed = speed;
        }
    }

    public boolean running = true;
    private Object platform = new Object(0.5, 1.0-PLATFORM_HEIGHT/2.0, 0.2, PLATFORM_HEIGHT);
    private List<Bubble> bubbles = new ArrayList<>();

    public Game() {
        rand = new Random();
    }

    public Object getPlatform() {
        return platform;
    }

    public List<Bubble> getBubbles() {
        return bubbles;
    }

    private boolean shouldBubbleSpawn() {
        return rand.nextDouble() < 1.0 / 60.0;
    }

    private void spawnBubble() {
        double randomSize = rand.nextDouble() * (MAX_BUBBLE_SIZE - MIN_BUBBLE_SIZE) + MIN_BUBBLE_SIZE;
        double randomSpeed = rand.nextDouble() * (MAX_BUBBLE_SPEED - MIN_BUBBLE_SPEED) + MIN_BUBBLE_SPEED;
        bubbles.add(new Bubble(randomSize, randomSpeed));
    }

    public void update(double delta, boolean left, boolean right) {
        delta = Math.min(delta, 1.0);

        int dir = left ? -1 : right ? 1 : 0;
        platform.x = Math.min(1.0 - platform.width/2, Math.max(platform.width/2, platform.x + PLATFORM_SPEED * delta * dir));

        for(Bubble bubble : bubbles) {
            bubble.y += bubble.speed * delta;
        }

        if(shouldBubbleSpawn()) {
            spawnBubble();
        }
    }
}
